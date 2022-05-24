import asyncio
import aiocoap.resource as resource
from aiocoap import *


class WhoAmI(resource.Resource):
    async def render_get(self, request):
        text = ["Used protocol: %s." % request.remote.scheme]

        text.append("Request came from %s." % request.remote.hostinfo)
        text.append("The server address used %s." % request.remote.hostinfo_local)

        claims = list(request.remote.authenticated_claims)
        if claims:
            text.append("Authenticated claims of the client: %s." % ", ".join(repr(c) for c in claims))
        else:
            text.append("No claims authenticated.")

        return Message(content_format=0,
                       payload="\n".join(text).encode('utf8'))


async def main():
    root = resource.Site()

    root.add_resource(['.well-known', 'core'],
                      resource.WKCResource(root.get_resources_as_linkheader))
    root.add_resource(['whoami'], WhoAmI())

    con = await Context.create_server_context(root, bind=("0.0.0.0", 5555))

    request = Message(code=GET, uri="coap://127.0.0.1:5683/resource-lookup/",
                      observe=0)
    req = con.request(request)
    res = await req.response
    print(res.payload)
    print("start async loop")
    async for r in req.observation:
        print(r.payload)
    # Run forever
    print("server running now")
    await asyncio.get_running_loop().create_future()


if __name__ == '__main__':
    asyncio.run(main())
