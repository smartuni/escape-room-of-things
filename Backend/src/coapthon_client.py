from coapthon.client.helperclient import HelperClient
host = "2001:db8::814c:35fc:fd31:5fde"
port = 5683


def get_led(led):
    path = "led/{}".format(led)

    client = HelperClient(server=(host, port))
    response = client.get(path)
    client.stop()
    print(response)
    return response.payload


def set_led(led, value):
    path = "led/{}".format(led)

    client = HelperClient(server=(host, port))
    response = client.put(path, payload=value)
    client.stop()
    print(response)
    return response.payload


def get_box(box):
    path = "box{}".format(box)

    client = HelperClient(server=(host, port))
    response = client.get(path)
    client.stop()
    print(response)
    return response.payload


def set_box(box, value):
    path = "box{}".format(box)

    client = HelperClient(server=(host, port))
    response = client.put(path, payload=value)
    client.stop()
    print(response)
    return response.payload


