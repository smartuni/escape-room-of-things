# project_be

# coapthon github
https://github.com/Tanganelli/CoAPthon
# flask and sqlite tutorial
https://levelup.gitconnected.com/full-stack-web-app-with-python-react-and-bootstrap-backend-8592baa6e4eb

# packages
# coapthon (for py3)
pip install CoAPthon3  
# aiocoap
pip install aiocoap  
# install flask package
pip install Flask  
# install flask_cors package
pip install Flask_cors  
# Only if you don't have sqlite installed
pip install db-sqlite3  


# api
path  
Payload if needed  
description  
response  

/getall (GET)
-> Return alls rooms with their puzzles
{'Roomname':['Puzzlename']}

/Rooms/room (DELETE)  
-> delete room and returns the deleted room  
{'name': text, 'state': 'text'}  

/Rooms/room (POST)  
-> adds room  
{'name': text, 'state': 'text'}  

/Rooms/room (PUT)  
Payload: {'state': text}  
-> update the state of room and returns the updated room  
{'name': text, 'state': 'text'}  

'/Rooms/room (GET)  
-> returns name and state of the room  
{'name': text, 'state': 'text'}  

/Rooms/movepuzzle/puzzle (PUT)  
Payload:{'newRoom': text, 'oldRoom': text}  
-> moves puzzle from oldRoom to newRoom and returns the room after update  
{'name': text, 'room':text, 'state': text}  

/Rooms/room/puzzle (GET)  
-> Returns info about puzzle in room  
{'name': text, 'room': text, 'state': text}  