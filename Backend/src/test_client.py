from coapthon.client.helperclient import HelperClient
host = "127.0.0.1"
port = 5683


def test():
    path = ".well-known/core"

    client = HelperClient(server=(host, port))
    response = client.get(path)
    client.stop()
    print(response.payload)


if __name__ == '__main__':
    test()
