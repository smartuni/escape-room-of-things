import asyncio
import aiocoap.resource as resource
from aiocoap import *


class node_info(resource.Resource):
    state = "solved"
    async def render_get(self, request):
        print("i've been called")
        return Message(content_format=0,
                       payload="\n".join(self.state).encode('utf8'))


async def main():

    root = resource.Site()


    root.add_resource(['/node/info'], node_info())

    await Context.create_server_context(root, bind=("127.0.0.1", 6666))
    
    # Run forever
    print("server running now")
    await asyncio.get_running_loop().create_future()


if __name__ == '__main__':
    asyncio.set_event_loop_policy(asyncio.WindowsSelectorEventLoopPolicy())
    asyncio.run(main())
