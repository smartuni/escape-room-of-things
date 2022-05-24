from flask import Flask, request, jsonify
from flask_cors import CORS
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:////tmp/escape_room.db'
db = SQLAlchemy(app)


# misc

# rooms

@app.route('/rooms', methods=['GET'])
def api_get_rooms():
    allEntries = {}
    rooms = Room.query.all()
    for room in rooms:
        allEntries[room.id] = [room, Puzzle.query.filter_by(room = room.id).all()]
    # todo: implement properly {rooms: [room[puzzle[device,...],...],...]}
    return jsonify(allEntries)


@app.route('/rooms/<roomid>', methods=['GET'])
def api_get_room(roomid):
    room = Room.query.filter_by(id=room).first()
    return jsonify(room.serialize())


@app.route('/rooms', methods=['POST'])
def api_add_room():
    request_data = request.get_json()
    room = Room(name=request_data['name'], description=request_data['description'], state=request_data['state'])
    db.session.add(room)
    db.session.commit()
    return jsonify(room.serialize())


@app.route('/rooms/<roomid>', methods=['PUT'])
def api_update_room(roomid):
    request_data = request.get_json()
    room = Room.query.filter_by(id=roomid).first()
    room.name = request_data['name']
    room.description = request_data['description']
    room.state = request_data['state']
    db.session.commit()
    return jsonify(room.serialize())


@app.route('/rooms/<roomid>', methods=['DELETE'])
def api_delete_room(roomid):
    room = Room.query.filter_by(id=roomid).first()
    db.session.delete(room)
    db.session.commit()
    return jsonify(room.serialize())


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
    puzzle = Puzzle(name=request_data['name'], description=request_data['description'], state=request_data['state'], room=request_data['room'])
    db.session.add(puzzle)
    db.session.commit()
    return jsonify(puzzle.serialize())


@app.route('/puzzles/<puzzleid>', methods=['PUT'])
def api_update_puzzle(puzzleid):
    request_data = request.get_json()
    puzzle = Puzzle.query.filter_by(id=puzzleid).first()
    puzzle.name = request_data['name']
    puzzle.description = request_data['description']
    puzzle.room = request_data['room']
    puzzle.state = request_data['state']
    db.session.commit()
    return jsonify(puzzle.serialize())


@app.route('/puzzles/<puzzleid>', methods=['DELETE'])
def api_delete_room(puzzleid):
    puzzle = Puzzle.query.filter_by(id=puzzleid).first()
    db.session.delete(puzzle)
    db.session.commit()
    return jsonify(puzzle.serialize())


# devices

@app.route('/devices', methods=['GET'])
def api_get_puzzles():
    puzzle = Puzzle.query.all()
    # todo: implement properly {puzzles: [puzzle[device,...],...]}
    return jsonify(puzzle.serialize())


@app.route('/devices/<deviceid>', methods=['GET'])
def api_get_puzzle(deviceid):
    device = Device.query.filter_by(id=deviceid).first()
    return jsonify(device.serialize())


@app.route('/devices/<deviceid>', methods=['PUT'])
def api_update_puzzle(deviceid):
    request_data = request.get_json()
    device = Device.query.filter_by(id=deviceid).first()
    device.state = request_data['state']
    device.puzzle = request_data['puzzle']
    db.session.commit()
    return jsonify(device.serialize())


@app.route('/devices/<deviceid>', methods=['DELETE'])
def api_delete_room(deviceid):
    device = Device.query.filter_by(id=deviceid).first()
    db.session.delete(device)
    db.session.commit()
    # todo: remove device from rd?
    return jsonify(device.serialize())


class Room(db.Model):
    id = db.Column(db.INTEGER, primary_key=True)
    name = db.Column(db.String(100), unique=False, nullable=False)
    description = db.Column(db.String(1000), unique=False, nullable=True)
    state = db.Column(db.String(20), unique=False, nullable=False)

    def __repr__(self):
        return 'ID: {}\nName: {}\nDescription: {}\nState: {}'.format(self.id, self.name, self.description, self.state)
    
    @property
    def serialize(self):
       """Return object data in easily serializable format"""
       return {
           'id': self.id,
           'name': self.name,
           'description': self.description,
           'state': self.state
       }

    @property
    def serialize_many2many(self):
       """
       Return object's relations in easily serializable format.
       NB! Calls many2many's serialize property.
       """
       return [ item.serialize for item in self.many2many]


class Puzzle(db.Model):
    id = db.Column(db.INTEGER, primary_key=True)
    name = db.Column(db.String(100), unique=False, nullable=False)
    description = db.Column(db.String(1000), unique=False, nullable=True)
    state = db.Column(db.String(20), unique=False, nullable=False)
    room = db.Column(db.INTEGER, db.ForeignKey('room.id'), nullable=False)

    def __repr__(self):
        return 'ID: {}\nName: {}\nDescription: {}\nState: {}\nRoom: {}'.format(self.id, self.name, self.description, self.state, self.room)
    
    @property
    def serialize(self):
        """Return object data in easily serializable format"""
        return {
            'id': self.id,
            'name': self.name,
            'description': self.description,
            'state': self.state,
            'room': self.room
        }


class Device(db.Model):
    id = db.Column(db.INTEGER, primary_key=True)
    devIP = db.Column(db.String(100), unique=False, nullable=False)
    devType = db.Column(db.String(20), unique=False, nullable=False)
    state = db.Column(db.String(20), unique=False, nullable=False)
    puzzle = db.Column(db.INTEGER, db.ForeignKey('puzzle.id'), nullable=False)

    def __repr__(self):
        return 'ID: {}\nDevice IP: {}\nDevice Type: {}\nState: {}\nPuzzle: {}'.format(self.id, self.devIP, self.devType, self.state, self.puzzle)

    @property
    def serialize(self):
        """Return object data in easily serializable format"""
        return {
            'id': self.id,
            'name': self.name,
            'description': self.description,
            'state': self.state,
            'puzzle': self.puzzle
        }


if __name__ == "__main__":
    db.create_all()
    app.run(host="0.0.0.0")
