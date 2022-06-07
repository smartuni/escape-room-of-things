import asyncio
import configparser
import os
import re

import aiocoap.resource as resource
from aiocoap import *
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


async def victory(room, con):
    victory_puzzle = next(
        filter(lambda puzzle: puzzle.isVictory.isTrue, room.puzzles))
    for dev in victory_puzzle:
        request = Message(code=PUT, uri=f"coap://{dev.devIP}/node/maintenance",
                          observe=1, payload=None)
        req = con.request(request)
        await req.response


async def check_game_state(device, con):
    puzzle_id = device.puzzle
    puzzle = Puzzle.query.filter_by(id=puzzle_id).first()
    if check_puzzle_state(puzzle):
        await trigger_event(puzzle, con)
        room_id = puzzle.room
        room = Room.query.filter_by(id=room_id).first()
        if check_room_state(room):
            await victory(room, con)


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


async def trigger_event(puzzle, con):
    event_devices = list(
        filter(lambda device: device.is_event_device, puzzle.devices))
    if len(event_devices) != 0:
        for dev in event_devices:
            request = Message(code=PUT, uri=f"coap://{dev.devIP}/node/maintenance",
                              observe=1, payload=None)
            req = con.request(request)
            await req.response


async def device_connected(devices, ips, con):
    print(devices)
    for dev in devices:
        if "base=" in dev:
            matches = re.search('ep="(.+?)";base="coap://(.+?)";', dev)
            if matches.group(2) not in ips:
                with db_app.app_context():
                    d = Device.query.filter_by(serial=matches.group(1)).first()
                    d.devIP = matches.group(2)
                    d.node_state = "connected"
                    db.session.commit()
                    ips.append(matches.group(2))
                    await observe_device(d, con)
    return ips


def get_devices_from_db():
    with db_app.app_context():
        devs = Device.query.all()
        return [d.devIP for d in devs]


async def observe_device(device, con):
    print(device.name)
    request = Message(code=GET, uri=f"coap://{device.devIP}/node/info",
                      observe=0)
    print(request)
    req = con.request(request)
    res = await req.response
    print(res)
    print(res.payload)

    print("observe: {}".format(device.devIP))
    async for r in req.observation:
        device.state = SOLVED
        db.session.commit()
        print(r)
        print(r.payload)
        # parse answer, CBOR?
        # implement if
        # check_game_state(device)


async def main():
    connectedIps = []
    # connectedIps = get_devices_from_db()
    root = resource.Site()

    root.add_resource(['.well-known', 'core'],
                      resource.WKCResource(root.get_resources_as_linkheader))
    root.add_resource(['whoami'], WhoAmI())

    con = await Context.create_server_context(root, bind=('fd00:dead:beef', 5555))
    # 127.0.0.1:5683
    request = Message(code=GET, uri="coap://fd00:dead:beef::1]:5683/endpoint-lookup/",
                      observe=0)
    req = con.request(request)
    res = await req.response
    cachedli = res.payload.decode('utf-8').split(",")
    connectedIps = await device_connected(cachedli, connectedIps, con)

    print("start async loop")
    async for r in req.observation:
        li = r.payload.decode('utf-8').split(",")

        connectedIps = await device_connected(li, connectedIps, con)

    # Run forever
    print("server running now")
    await asyncio.get_running_loop().create_future()


if __name__ == '__main__':
    # asyncio.set_event_loop_policy(asyncio.WindowsSelectorEventLoopPolicy())
    asyncio.run(main())
