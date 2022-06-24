from coapthon_client import *
from sqlite import *
from flask import Flask, request, jsonify, render_template
from flask_cors import CORS

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})


# get all rooms and puzzles 
@app.route('/getall', methods=['GET'])
def api_getall():
    response = get_rooms_with_puzzles()
    return jsonify(response)


# rooms

@app.route('/Rooms/<room>', methods=['DELETE'])
def api_delete_room(room):
    response = delete_room(room)
    return jsonify(response)


@app.route('/Roomm/<room>', methods=['POST'])
def api_add_room(room):
    response = add_room(room)
    return jsonify(response)

@app.route('/Rooms/<room>', methods=['PUT'])
def api_update_room(room):
    request_data = request.get_json()
    response = update_room(request_data['state'],room)
    return jsonify(response)


@app.route('/Rooms/<room>', methods=['GET'])
def api_get_room(room):
    response = get_room_by_name(room)
    return jsonify(response)


@app.route('/Rooms/movepuzzle/<puzzle>', methods=['PUT'])
def api_moovepuzzle(puzzle):
    request_data = request.get_json()
    response = update_puzzle(request_data['newRoom'],[request_data['oldRoom']], puzzle)
    return jsonify({'name': response.name, 'room':response.room, 'state': response.state})


# puzzles
@app.route('/Rooms/<room>/<puzzle>', methods=['GET'])
def api_get_puzzle(room, puzzle):
    response = get_room_by_name(room, puzzle)
    return jsonify({'name': response.name, 'room':response.room, 'state': response.state})


# coap
@app.route('/coap/led<id>', methods=['GET'])
def api_get_led_value(id):
    response = get_led(id)
    return jsonify(response)


@app.route('/coap/led<id>', methods=['POST'])
def api_set_led_value(id):
    response = set_led(id, request.get_json()["value"])
    return jsonify(response)


@app.route('/coap/box<id>', methods=['GET'])
def api_get_box_value(id):
    response = get_box(id)
    return jsonify(response)


@app.route('/coap/box<id>', methods=['POST'])
def api_set_box_value(id):
    response = set_box(id, request.get_json()["value"])
    return jsonify(response)


if __name__ == "__main__":
    # from waitress import serve

    # serve(app, host="0.0.0.0")
    app.run(host="0.0.0.0")
