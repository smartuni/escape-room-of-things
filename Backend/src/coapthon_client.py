from coapthon.client.helperclient import HelperClient
host = "127.0.0.1"
port = 5683


def get_led(led):
    path = "led{}".format(led)

    client = HelperClient(server=(host, port))
    response = client.get(path)
    client.stop()
    print(response)
    return response.payload


def set_led(led, value):
    path = "led{}".format(led)

    client = HelperClient(server=(host, port))
    response = client.put(path, payload=value)
    client.stop()
    print(response)
    return response.payload
