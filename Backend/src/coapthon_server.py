from coapthon.resources.resource import Resource
from coapthon.server.coap import CoAP
from coapthon import defines


class LEDResource(Resource):
    def __init__(self, name="led", coap_server=None):
        super(LEDResource, self).__init__(name, coap_server, visible=True,
                                          observable=True, allow_children=True)
        self.state = "0"

    def render_PUT_advanced(self, request, response):
        self.state = request.payload
        from coapthon.messages.response import Response
        assert(isinstance(response, Response))
        response.payload = self.state
        response.code = defines.Codes.CHANGED.number
        return self, response

    def render_GET_advanced(self, request, response):
        response.payload = self.state
        response.max_age = 20
        response.code = defines.Codes.CONTENT.number
        return self, response


class CoAPServer(CoAP):
    def __init__(self, host, port):
        CoAP.__init__(self, (host, port))
        self.add_resource('led0', LEDResource(name='led0'))
        self.add_resource('led1', LEDResource(name='led1'))
        self.add_resource('led2', LEDResource(name='led2'))
        self.add_resource('led3', LEDResource(name='led3'))


def main():
    print("Server start")
    server = CoAPServer("127.0.0.1", 5683)
    try:
        server.listen(10)
    except KeyboardInterrupt:
        print("Server Shutdown")
        server.close()
        print("Exiting...")


if __name__ == '__main__':
    main()
