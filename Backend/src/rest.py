import os
import sys
from flask import Flask, request, jsonify
from flask_cors import CORS
from orm_classes.shared import db
from orm_classes.Device import Device
from orm_classes.Puzzle import Puzzle
from orm_classes.Room import Room
import configparser


READY = "ready"
ID = "id"
NAME = "name"
DESCRIPTION = "description"
STATE = "state"
ROOM = "room"
PUZZLE = "puzzle"


config = configparser.ConfigParser()
config.read(os.path.join(os.path.dirname(__file__),'restconfig.ini'))

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + \
    config.get('database', 'path')
db.init_app(app)


# misc

@app.route('/rooms/state/<roomid>', methods=['PUT'])
def api_update_room_state(roomid):
    request_data = request.get_json()
    room = Room.query.filter_by(id=roomid).first()
    room.state = request_data[STATE]
    db.session.commit()
    return jsonify(room.serialize())


@app.route('/puzzles/state/<puzzleid>', methods=['PUT'])
def api_update_puzzle_state(puzzleid):
    request_data = request.get_json()
    puzzle = Puzzle.query.filter_by(id=puzzleid).first()
    puzzle.state = request_data[STATE]
    db.session.commit()
    return jsonify(puzzle.serialize())


# rooms

@app.route('/rooms', methods=['GET'])
def api_get_rooms():
    rooms = Room.query.all()
    return jsonify({'rooms': serialize_rooms(rooms)})


@app.route('/rooms/<roomid>', methods=['GET'])
def api_get_room(roomid):
    room = Room.query.filter_by(id=roomid).first()
    return jsonify(room.serialize())


@app.route('/rooms', methods=['POST'])
def api_add_room():
    request_data = request.get_json()
    room = Room(name=request_data[NAME],
                description=request_data[DESCRIPTION],
                state=READY)
    db.session.add(room)
    db.session.commit()
    return jsonify(room.serialize())


@app.route('/rooms/<roomid>', methods=['PUT'])
def api_update_room(roomid):
    request_data = request.get_json()
    room = Room.query.filter_by(id=roomid).first()
    room.name = request_data[NAME]
    room.description = request_data[DESCRIPTION]
    db.session.commit()
    return jsonify(room.serialize())


@app.route('/rooms/<roomid>', methods=['DELETE'])
def api_delete_room(roomid):
    room = Room.query.filter_by(id=roomid).first()
    roomcopy = room.serialize()
    db.session.delete(room)
    db.session.commit()
    return jsonify(roomcopy)


# puzzles

@app.route('/puzzles', methods=['GET'])
def api_get_puzzles():
    puzzles = Puzzle.query.all()
    return jsonify({'puzzles': serialize_puzzles(puzzles)})


@app.route('/puzzles/<puzzleid>', methods=['GET'])
def api_get_puzzle(puzzleid):
    puzzle = Puzzle.query.filter_by(id=puzzleid).first()
    return jsonify(puzzle.serialize())


@app.route('/puzzles', methods=['POST'])
def api_add_puzzle():
    request_data = request.get_json()
    puzzle = Puzzle(name=request_data[NAME],
                    description=request_data[DESCRIPTION],
                    state=READY,
                    room=request_data[ROOM])
    db.session.add(puzzle)
    db.session.commit()
    return jsonify(puzzle.serialize())


@app.route('/puzzles/<puzzleid>', methods=['PUT'])
def api_update_puzzle(puzzleid):
    request_data = request.get_json()
    puzzle = Puzzle.query.filter_by(id=puzzleid).first()
    puzzle.name = request_data[NAME]
    puzzle.description = request_data[DESCRIPTION]
    puzzle.room = request_data[ROOM]
    db.session.commit()
    return jsonify(puzzle.serialize())


@app.route('/puzzles/<puzzleid>', methods=['DELETE'])
def api_delete_puzzle(puzzleid):
    puzzle = Puzzle.query.filter_by(id=puzzleid).first()
    puzzlecopy = puzzle.serialize()
    db.session.delete(puzzle)
    db.session.commit()
    return jsonify(puzzlecopy)


# devices

@app.route('/devices', methods=['GET'])
def api_get_devices():
    devices = Device.query.all()
    return jsonify({'devices': serialize_devices(devices)})


@app.route('/devices/<deviceid>', methods=['GET'])
def api_get_device(deviceid):
    device = Device.query.filter_by(id=deviceid).first()
    return jsonify(device.serialize())


@app.route('/devices/<deviceid>', methods=['PUT'])
def api_update_device(deviceid):
    request_data = request.get_json()
    device = Device.query.filter_by(id=deviceid).first()
    device.state = request_data[STATE]
    device.puzzle = request_data[PUZZLE]
    db.session.commit()
    return jsonify(device.serialize())


@app.route('/devices/<deviceid>', methods=['DELETE'])
def api_delete_device(deviceid):
    device = Device.query.filter_by(id=deviceid).first()
    devicecopy = device.serialize()
    db.session.delete(device)
    db.session.commit()
    # todo: remove device from rd?
    return jsonify(devicecopy)


# function to add the default room and default puzzle for unassigned devices

def add_default_room_and_puzzle():
    room = Room(id="0",
                name="default",
                description="Default room for unassigned devices",
                state=READY)
    puzzle = Puzzle(id="0",
                    name="default",
                    description="Default puzzle for unassigned devices",
                    room="0",
                    state=READY)
    db.session.add(room)
    db.session.add(puzzle)
    db.session.commit()


# functions for serializing lists of objects for the get all paths

def serialize_rooms(rooms):
    serializedRooms = []
    for r in rooms:
        sr = r.serialize()
        sr['puzzles'] = serialize_puzzles(r.puzzles)
        serializedRooms.append(sr)
    return serializedRooms


def serialize_puzzles(puzzles):
    serializedPuzzles = []
    for p in puzzles:
        sp = p.serialize()
        sp['devices'] = serialize_devices(p.devices)
        serializedPuzzles.append(sp)
    return serializedPuzzles


def serialize_devices(devices):
    serializedDevices = []
    for d in devices:
        serializedDevices.append(d.serialize())
    return serializedDevices


# create functions for testing purposes (can be called in app_context in main)

def new_room(number=1):
    rooms = []
    for _ in range(number):
        rooms.append(Room(name="testroom", description="", state=READY))
    db.session.add_all(rooms)
    db.session.commit()


def new_puzzle(roomid, number=1):
    puzzles = []
    for _ in range(number):
        puzzles.append(Puzzle(name="testpuzzle",
                              description="",
                              room=roomid,
                              state=READY))
    db.session.add_all(puzzles)
    db.session.commit()


def new_device(puzzleid, number=1):
    devices = []
    for _ in range(number):
        devices.append(Device(name="testdevice",
                              devIP="",
                              description="",
                              puzzle=puzzleid,
                              state=READY))
    db.session.add_all(devices)
    db.session.commit()


if __name__ == "__main__":
    # uncomment for first time to create db and default room + puzzle
    # todo: think of better solution
    if len(sys.argv) > 1:
        with app.app_context():
            db.create_all()
            add_default_room_and_puzzle()
    app.run(host="0.0.0.0")
