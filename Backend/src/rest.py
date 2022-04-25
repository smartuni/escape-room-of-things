from coapthon_client import get_led, set_led
from sqlite import update_led, insert_led, get_led_by_name
from flask import Flask, request, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})


# coap
@app.route('/coap/led<id>', methods=['GET'])
def api_get_led_value(id):
    response = get_led(id)
    return jsonify(response)


@app.route('/coap/led<id>', methods=['POST'])
def api_set_led_value(id):
    response = set_led(id, request.get_json()["value"])
    return jsonify(response)


# db
@app.route('/db/led<id>', methods=['GET'])
def api_db_get_led_value(id):
    return jsonify(get_led_by_name("led" + id))


@app.route('/db/led<id>', methods=['POST'])
def api_db_set_led_value(id):
    request_data = request.get_json()
    to_update = {"name": "led{}".format(id),
                 "value": request_data["value"]}
    return(update_led(to_update))


@app.route('/db/add/led<id>', methods=['POST'])
def api_db_add_led(id):
    return jsonify(insert_led("led{}".format(id)))


if __name__ == "__main__":
    app.run(host="0.0.0.0")
