import configparser
import datetime
import os
import sys
import uuid
from functools import wraps

import jwt
from flask import Flask, request, jsonify, make_response
from flask_cors import CORS
from werkzeug.security import generate_password_hash, check_password_hash

from orm_classes.Device import Device
from orm_classes.Puzzle import Puzzle
from orm_classes.Room import Room
from orm_classes.User import User
from orm_classes.shared import db

SOLVED = "solved"
READY = "ready"
ID = "id"
NAME = "name"
DESCRIPTION = "description"
STATE = "state"
ROOM = "room"
PUZZLE = "puzzle"
IS_EVENT_DEVICE = "is_event_device"
SERIAL = "serial"
CONNECTED = 'connected'
UNCONNECTED = 'unconnected'
DEVIP = 'devIP'
PUBKEY = 'pubkey'
ADMIN = 'admin'
config = configparser.ConfigParser()
config.read(os.path.join(os.path.dirname(__file__), 'restconfig.ini'))

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + \
                                        config.get('database', 'path')
app.config['SECRET_KEY'] = 'b8f12c836b29c5d6c1ab1813d5a4c926'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True
db.init_app(app)


def user_token_required(f):
    @wraps(f)
    def decorator(*args, **kwargs):
        token = None
        if 'x-access-tokens' in request.headers:
            token = request.headers['x-access-tokens']

        if not token:
            return jsonify({'message': 'a valid token is missing'})
        try:
            data = jwt.decode(token, app.config['SECRET_KEY'], algorithms=["HS256"])
            current_user = User.query.filter_by(public_id=data['public_id']).first()

        except:
            return jsonify({'message': 'token is invalid'})

        return f(*args, **kwargs)

    return decorator


def admin_token_required(f):
    @wraps(f)
    def decorator(*args, **kwargs):
        token = None
        if 'x-access-tokens' in request.headers:
            token = request.headers['x-access-tokens']

        if not token:
            return jsonify({'message': 'a valid token is missing'})
        try:
            data = jwt.decode(token, app.config['SECRET_KEY'], algorithms=["HS256"])
            current_user = User.query.filter_by(public_id=data['public_id']).first()
            if not current_user.admin:
                return jsonify({'message': '403 Forbidden access'})

        except:
            return jsonify({'message': 'token is invalid'})

        return f(*args, **kwargs)

    return decorator


# misc

@app.route('/rooms/state/<roomid>', methods=['PUT'])
@admin_token_required
def api_update_room_state(roomid):
    request_data = request.get_json()
    room = Room.query.filter_by(id=roomid).first()
    room.state = request_data[STATE]
    db.session.commit()
    return jsonify(serialize_rooms([room])[0])


@app.route('/puzzles/state/<puzzleid>', methods=['PUT'])
@admin_token_required
def api_update_puzzle_state(puzzleid):
    request_data = request.get_json()
    puzzle = Puzzle.query.filter_by(id=puzzleid).first()
    puzzle.state = request_data[STATE]
    db.session.commit()
    return jsonify(serialize_puzzles([puzzle])[0])


@app.route('/devices/state/<deviceid>', methods=['PUT'])
@admin_token_required
def api_update_device_state(deviceid):
    request_data = request.get_json()
    device = Device.query.filter_by(id=deviceid).first()
    device.state = request_data[STATE]
    db.session.commit()
    return jsonify(device.serialize())


# rooms
@app.route('/rooms', methods=['GET'])
@user_token_required
def api_get_rooms():
    rooms = Room.query.all()
    return jsonify({'rooms': serialize_rooms(rooms)})


@app.route('/rooms/<roomid>', methods=['GET'])
@admin_token_required
def api_get_room(roomid):
    room = Room.query.filter_by(id=roomid).first()
    return jsonify(room.serialize())


@app.route('/rooms', methods=['POST'])
@admin_token_required
def api_add_room():
    request_data = request.get_json()
    room = Room(name=request_data[NAME],
                description=request_data[DESCRIPTION],
                state=READY)
    db.session.add(room)
    db.session.commit()
    puzzle = Puzzle(name="victoryPuzzle",
                    description="This Puzzle contains the Victory-Event",
                    state=SOLVED,
                    isVictory=True,
                    room=room.id)
    db.session.add(puzzle)
    db.session.commit()
    return jsonify(serialize_rooms([room])[0])


@app.route('/rooms/<roomid>', methods=['PUT'])
@admin_token_required
def api_update_room(roomid):
    request_data = request.get_json()
    room = Room.query.filter_by(id=roomid).first()
    room.name = request_data[NAME]
    room.description = request_data[DESCRIPTION]
    db.session.commit()
    return jsonify(serialize_rooms([room])[0])


@app.route('/rooms/<roomid>', methods=['DELETE'])
@admin_token_required
def api_delete_room(roomid):
    if roomid == "0":
        return "Can't delete default room", 400
    room = Room.query.filter_by(id=roomid).first()
    for puzzle in room.puzzles:
        for dev in puzzle.devices:
            dev.puzzle = "0"
        db.session.commit()
        db.session.delete(puzzle)
    db.session.commit()
    roomcopy = room.serialize()
    db.session.delete(room)
    db.session.commit()
    return jsonify(roomcopy)


# puzzles

@app.route('/puzzles', methods=['GET'])
@admin_token_required
def api_get_puzzles():
    puzzles = Puzzle.query.all()
    return jsonify({'puzzles': serialize_puzzles(puzzles)})


@app.route('/puzzles/<puzzleid>', methods=['GET'])
@admin_token_required
def api_get_puzzle(puzzleid):
    puzzle = Puzzle.query.filter_by(id=puzzleid).first()
    return jsonify(serialize_puzzles([puzzle])[0])


@app.route('/puzzles', methods=['POST'])
@admin_token_required
def api_add_puzzle():
    request_data = request.get_json()
    puzzle = Puzzle(name=request_data[NAME],
                    description=request_data[DESCRIPTION],
                    state=READY,
                    room=request_data[ROOM],
                    isVictory=False)
    db.session.add(puzzle)
    db.session.commit()
    return jsonify(serialize_puzzles([puzzle])[0])


@app.route('/puzzles/<puzzleid>', methods=['PUT'])
@admin_token_required
def api_update_puzzle(puzzleid):
    request_data = request.get_json()
    puzzle = Puzzle.query.filter_by(id=puzzleid).first()
    puzzle.name = request_data[NAME]
    puzzle.description = request_data[DESCRIPTION]
    puzzle.room = request_data[ROOM]
    db.session.commit()
    return jsonify(serialize_puzzles([puzzle])[0])


@app.route('/puzzles/<puzzleid>', methods=['DELETE'])
@admin_token_required
def api_delete_puzzle(puzzleid):
    if puzzleid == "0":
        return "Can't delete default puzzle", 400
    puzzle = Puzzle.query.filter_by(id=puzzleid).first()
    for dev in puzzle.devices:
        dev.puzzle = "0"
    db.session.commit()
    puzzlecopy = puzzle.serialize()
    db.session.delete(puzzle)
    db.session.commit()
    return jsonify(puzzlecopy)


# devices

@app.route('/devices', methods=['GET'])
@admin_token_required
def api_get_devices():
    devices = Device.query.all()
    return jsonify({'devices': serialize_devices(devices)})


@app.route('/devices/<deviceid>', methods=['GET'])
@admin_token_required
def api_get_device(deviceid):
    device = Device.query.filter_by(id=deviceid).first()
    return jsonify(device.serialize())


@app.route('/devices', methods=['POST'])
@admin_token_required
def api_add_device():
    request_data = request.get_json()

    is_event_device = request_data[IS_EVENT_DEVICE] if IS_EVENT_DEVICE in request_data else False
    puzzle = request_data[PUZZLE] if PUZZLE in request_data else "0"
    description = request_data[DESCRIPTION] if DESCRIPTION in request_data else ""
    name = request_data[NAME] if NAME in request_data else False

    device = Device(
        serial=request_data[SERIAL],
        pubkey=request_data[PUBKEY],
        name=name,
        description=description,
        puzzle=puzzle,
        is_event_device=is_event_device,
        state=READY,
        node_state=UNCONNECTED
    )
    db.session.add(device)
    db.session.commit()
    return jsonify(device.serialize())


@app.route('/devices/<deviceid>', methods=['PUT'])
@admin_token_required
def api_update_device(deviceid):
    request_data = request.get_json()
    device = Device.query.filter_by(id=deviceid).first()
    device.puzzle = request_data[PUZZLE]
    if IS_EVENT_DEVICE in request_data:
        device.is_event_device = request_data[IS_EVENT_DEVICE]
    db.session.commit()
    return jsonify(device.serialize())


@app.route('/devices/<deviceid>', methods=['DELETE'])
@admin_token_required
def api_delete_device(deviceid):
    device = Device.query.filter_by(id=deviceid).first()
    devicecopy = device.serialize()
    db.session.delete(device)
    db.session.commit()
    # todo: remove device from rd?
    return jsonify(devicecopy)


# User-Handling

@app.route('/register', methods=['POST'])
def signup_user():
    request_data = request.get_json()
    hashed_password = generate_password_hash(request_data['password'], method='sha256')

    new_user = User(username=request_data['username'],
                    public_id=str(uuid.uuid4()),
                    password=hashed_password,
                    admin=False)
    db.session.add(new_user)
    db.session.commit()
    return jsonify(new_user.serialize())


@app.route('/login', methods=['POST'])
def login():
    auth = request.authorization
    print(auth)
    if not auth or not auth.username or not auth.password:
        return make_response('could not verify', 401, {'Authentication': 'login required"'})

    user = User.query.filter_by(username=auth.username).first()
    if check_password_hash(user.password, auth.password):
        token = jwt.encode(
            {'public_id': user.public_id, 'exp': datetime.datetime.utcnow() + datetime.timedelta(hours=1)},
            app.config['SECRET_KEY'], "HS256")
        return jsonify({'token': token.decode('UTF-8')})
    return make_response('could not verify', 401, {'Authentication': '"login required"'})


@app.route('/users', methods=['GET'])
@admin_token_required
def get_all_users():
    users = User.query.all()
    return jsonify({'users': serialize_users(users)})


@app.route('/users/<userid>', methods=['DELETE'])
@admin_token_required
def delete_user(userid):
    user = db.query.filter_by(id=userid).first()
    user_copy = user.serialize()
    db.session.delete(user)
    db.session.commit()
    return jsonify(user_copy)


@app.route('/users/<userid>', methods=['PUT'])
@admin_token_required
def change_admin_att(userid):
    request_data = request.get_json()
    user = db.query.filter_by(id=userid).first()
    user.admin = request_data[ADMIN]


# function to add the default room and default puzzle for unassigned devices

def add_default_room_and_puzzle_and_admin():
    room = Room(id="0",
                name="default",
                description="Default room for unassigned devices",
                state=READY)
    puzzle = Puzzle(id="0",
                    name="default",
                    description="Default puzzle for unassigned devices",
                    room="0",
                    state=READY)
    hashed_password = generate_password_hash('admin', method='sha256')
    admin = User(id="0",
                 public_id=str(uuid.uuid4()),
                 username="admin",
                 password=hashed_password,
                 admin=True)
    db.session.add(room)
    db.session.add(puzzle)
    db.session.add(admin)
    db.session.commit()


# functions for serializing lists of objects for the get all paths

def serialize_rooms(rooms):
    serialized_rooms = []
    for r in rooms:
        sr = r.serialize()
        sr['puzzles'] = serialize_puzzles(r.puzzles)
        serialized_rooms.append(sr)
    return serialized_rooms


def serialize_puzzles(puzzles):
    serialized_puzzles = []
    for p in puzzles:
        sp = p.serialize()
        sp['devices'] = serialize_devices(p.devices)
        serialized_puzzles.append(sp)
    return serialized_puzzles


def serialize_devices(devices):
    serialized_devices = []
    for d in devices:
        serialized_devices.append(d.serialize())
    return serialized_devices


def serialize_users(users):
    serialized_users = []
    for u in users:
        serialized_users.append(u.serialize())
    return serialized_users


# functions for User-Handling


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
    if len(sys.argv) > 1:
        with app.app_context():
            db.create_all()
            add_default_room_and_puzzle_and_admin()
    app.run(host="0.0.0.0")
