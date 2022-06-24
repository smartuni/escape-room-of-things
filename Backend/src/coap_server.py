import asyncio
import configparser
import os
import platform
import re
import sys
import time
import random

import aiocoap.resource as resource
import cbor2
from aiocoap import *
from aiocoap.credentials import CredentialsMap
from aiocoap.numbers.codes import GET, PUT
from flask import Flask

from orm_classes.Device import Device
from orm_classes.Puzzle import Puzzle
from orm_classes.Room import Room
from orm_classes.shared import db

SOLVED = "solved"
config = configparser.ConfigParser()
config.read(os.path.join(os.path.dirname(__file__), 'restconfig.ini'))
db_app = Flask(__name__)
db_app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + \
                                           config.get('database', 'path')
db.init_app(db_app)
observe_tasks = {}

class WhoAmI(resource.Resource):
    async def render_get(self, request):
        text = ["Used protocol: %s." % request.remote.scheme]

        text.append("Request came from %s." % request.remote.hostinfo)
        text.append("The server address used %s." %
                    request.remote.hostinfo_local)

        claims = list(request.remote.authenticated_claims)
        if claims:
            text.append("Authenticated claims of the client: %s." %
                        ", ".join(repr(c) for c in claims))
        else:
            text.append("No claims authenticated.")

        return Message(content_format=0,
                       payload="\n".join(text).encode('utf8'))


class DTLSCredential(CredentialsMap):
    psk = ""

    def credentials_from_request(self, msg):
        print("START DTLS connection")
        print(msg)
        print(self.psk)
        return self.psk


class coap_server:
    async def init(self):
        self.connectedDevices = []
        # self.get_devices_from_db()
        root = resource.Site()
        server_credentials = DTLSCredential()
        self.con = await Context.create_server_context(root, bind=('::1', 5555))

    async def device_connected(self, devices):
        print('check for divices to connect')
        for dev in devices:
            if "base=" in dev and "ep=" in dev:
                matches = re.search('ep="(.+?)";base="coap://(.+?)";', dev)
                with db_app.app_context():
                    d = Device.query.filter_by(serial=matches.group(1)).first()
                    if d is not None and matches.group(1) not in self.connectedDevices:
                        d.devIP = matches.group(2)
                        d.node_state = "connected"
                        self.connectedDevices.append(matches.group(1))
                        print(matches.group(1) + " connected")
                        task = asyncio.create_task(self.observe_device(d.serial), name=d.name)
                        observe_tasks[d.serial] = task
                    db.session.commit()
                    time.sleep(0.5 + random.random())
                        #await self.observe_device(d.serial)

    def device_disconnected(self, devices):
        print('check for divices to disconnect')
        con_serials = []
        removed_serials = []
        for dev in devices:
            if "ep=" in dev:
                matches = re.search('ep="(.+?)";', dev)
                con_serials.append(matches.group(1))
        print(con_serials)
        for serial in self.connectedDevices:
            if serial not in con_serials:
                with db_app.app_context():
                    d = Device.query.filter_by(serial=serial).first()
                    d.node_state = "disconnected"
                    db.session.commit()
                removed_serials.append(serial)
                print(serial + " disconnected")
        self.connectedDevices = [
            x for x in self.connectedDevices if x not in removed_serials]

    def get_devices_from_db(self):
        with db_app.app_context():
            devs = Device.query.all()
        self.connectedDevices = [d.serial for d in devs]

    async def observe_device(self, serial):
        print('start observe on ' + serial)
        with db_app.app_context():
            try:
                device = Device.query.filter_by(serial=serial).first()
                deviceIp = device.devIP
                devicePsk = device.psk
                deviceQrid = device.qrid
                db.session.commit()
                if dtls:
                    con, uri = await get_client_con_dtls(deviceIp, devicePsk, deviceQrid , "info")
                else:
                    con, uri = await get_client_con(device, "info")
            except Exception as e:
                print('make con troubles')
                print(e)
        request = Message(code=GET, uri=uri, observe=0)
        req = con.request(request)

        res = await req.response
        unpacked = cbor2.loads(res.payload)
        print(unpacked)
        with db_app.app_context():
            try:
                device = Device.query.filter_by(serial=serial).first()
                device.state = unpacked["puzzleState"]
                db.session.commit()
            except Exception as e:
                print('setting state troubles')
                print(e)
        # trigger cascading logic to check for solved
        if unpacked["puzzleState"] == "solved":
            with db_app.app_context():
                try:
                    device = Device.query.filter_by(serial=serial).first()
                    await check_game_state(device)
                    db.session.commit()
                except Exception as e:
                    print('setting game state troubles')
                    print(e)

        async for r in req.observation:
            unpacked = cbor2.loads(r.payload)
            print(unpacked)
            with db_app.app_context():
                device = Device.query.filter_by(serial=serial).first()
                device.state = unpacked["puzzleState"]
                db.session.commit()
            # trigger cascading logic to check for solved
            if unpacked["puzzleState"] == "solved":
                with db_app.app_context():
                    device = Device.query.filter_by(serial=serial).first()
                    await check_game_state(device)
                    db.session.commit()

    async def observe_rd(self):
        request = Message(code=GET, uri="coap://[::1]:5683/endpoint-lookup/",
                          observe=0)
        req = self.con.request(request)
        res = await req.response
        cachedli = res.payload.decode('utf-8').split(",")
        await self.device_connected(cachedli)

        print("start observe loop")
        async for r in req.observation:
            li = r.payload.decode('utf-8').split(",")
            await self.device_connected(li)

    async def poll_rd(self):
        print("start poll loop")
        while True:
            connect_check_request = Message(code=GET, uri="coap://[::1]:5683/endpoint-lookup/",
                                            observe=1)
            con_req = self.con.request(connect_check_request)
            con_res = await con_req.response
            con_devices = con_res.payload.decode('utf-8').split(",")
            print(con_devices)
            self.device_disconnected(con_devices)
            await self.device_connected(con_devices)
            await asyncio.sleep(15)


# PUZZLE LOGIC----------------------------------------

async def victory(room):
    victory_puzzle = next(
        filter(lambda puzzle: puzzle.isVictory.isTrue, room.puzzles))

    for dev in victory_puzzle:
        if dtls:
            con, uri = await get_client_con_dtls(dev, "maintenance")
        else:
            con, uri = await get_client_con(dev, "maintenance")
        request = Message(code=PUT, uri=uri,
                          observe=1, payload=None)
        req = con.request(request)
        await req.response


async def check_game_state(device):
    puzzle_id = device.puzzle
    puzzle = Puzzle.query.filter_by(id=puzzle_id).first()
    if check_puzzle_state(puzzle):
        await trigger_event(puzzle)
        room_id = puzzle.room
        room = Room.query.filter_by(id=room_id).first()
        if check_room_state(room):
            await victory(room)


def check_room_state(room):
    for puz in room.puzzles:
        if puz.id == 0:
            continue
        if puz.state != SOLVED:
            return False
    room.state = SOLVED
    db.session.commit()
    return True


def check_puzzle_state(puzzle):
    for dev in puzzle.devices:
        if dev.state != SOLVED:
            return False
    puzzle.state = SOLVED
    db.session.commit()
    return True


def set_device_solved(device):
    device.state = SOLVED
    db.session.commit()


async def trigger_event(puzzle):
    event_devices = list(
        filter(lambda device: device.is_event_device, puzzle.devices))
    if len(event_devices) != 0:
        for dev in event_devices:
            if dtls:
                con, uri = await get_client_con_dtls(dev, "maintenance")
            else:
                con, uri = await get_client_con(dev, "maintenance")
            request = Message(code=PUT, uri=uri,
                              observe=1, payload=None)
            req = con.request(request)
            await req.response


async def get_client_con_dtls(deviceIP, devicePsk, devoceQrid, path):
    con = await Context.create_client_context()
    uri = f"coaps://{deviceIP}:5684/node/{path}"
    print(devicePsk+ ' ' + devoceQrid)
    con.client_credentials.load_from_dict(
        {uri: {'dtls': {'psk': devicePsk.encode(), 'client-identity': devoceQrid.encode()}}})
    return con, uri


async def get_client_con(device, path):
    con = await Context.create_client_context()
    uri = f"coaps://{device.devIP}:5684/node/{path}"
    print(device.psk + ' ' + device.qrid)
    return con, uri


async def main():
    server = coap_server()
    await server.init()

    tasks = map(asyncio.create_task, [server.poll_rd()])
    await asyncio.wait(tasks)

    print("server running now")
    await asyncio.get_running_loop().create_future()

dtls = False
if __name__ == '__main__':
    if len(sys.argv) > 1:
        dtls = True
    if platform.system() == 'Windows':
        asyncio.set_event_loop_policy(asyncio.WindowsSelectorEventLoopPolicy())
    asyncio.run(main())
