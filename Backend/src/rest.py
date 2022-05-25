from flask import Flask, request, jsonify
from flask_cors import CORS
from flask_sqlalchemy import SQLAlchemy
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
config.read('Backend/src/restconfig.ini')

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + config.get('database', 'path')
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
    allEntries = {'rooms': []}
    allRooms = []
    rooms = Room.query.all()
    for room in rooms:
        allRooms.append(room)
        # todo: implement properly {rooms: [room[puzzle[device,...],...],...]}
    print(allRooms)
    return jsonify({'rooms': allRooms})


@app.route('/rooms/<roomid>', methods=['GET'])
def api_get_room(roomid):
    room = Room.query.filter_by(id=roomid).first()
    return jsonify(room.serialize())


@app.route('/rooms', methods=['POST'])
def api_add_room():
    request_data = request.get_json()
    room = Room(name=request_data[NAME], description=request_data[DESCRIPTION], state=READY)
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
    puzzle = Puzzle.query.all()
    # todo: implement properly {puzzles: [puzzle[device,...],...]}
    return jsonify(puzzle.serialize())


@app.route('/puzzles/<puzzleid>', methods=['GET'])
def api_get_puzzle(puzzleid):
    puzzle = Puzzle.query.filter_by(id=puzzleid).first()
    return jsonify(puzzle.serialize())


@app.route('/puzzles', methods=['POST'])
def api_add_puzzle():
    request_data = request.get_json()
    puzzle = Puzzle(name=request_data[NAME], description=request_data[DESCRIPTION], state=READY,
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
    puzzle = Puzzle.query.all()
    # todo: implement properly {puzzles: [puzzle[device,...],...]}
    return jsonify(puzzle.serialize())


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


if __name__ == "__main__":
    # db.create_all()
    print('soup')
    print(app.config['SQLALCHEMY_DATABASE_URI'])
    app.run(host="0.0.0.0")
