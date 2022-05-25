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
# install Flask SQLAlchemy
pip install -U Flask-SQLAlchemy


# api

/path
-> description
Req:{Request}
Res:{Response}

##misc

##rooms

/rooms (GET)
->returns all rooms with puzzles and devices
Res:
{'rooms': [{
			'id': text
		    'name': text,
		    'description': text,
		    'state': text,
		    'puzzles': [{
				'id': text,
				'name': text,
				'description': text,
				'state': text,
				'room': text,
				'devices':[{
					'id': text,
					'devType': text,
					'state': text,
					'puzzle': text,
				},...]
			},...]
		},...]
}


/rooms/{roomid} (GET)
->returns room for roomid in path
Res:
{
	'id': text
	'name': text,
	'description': text,
	'state': text,
}

/rooms (POST)
->adds room from request and returns the new room
Req:
{
	'name': text,
	'description': text,
	'state': text
}

Res:
{
	'id': text
	'name': text,
	'description': text,
	'state': text,
}

/rooms/{roomid} (PUT)
->updates room for roomid from path and returns the updated room
Req:
{
	'name': text,
	'description': text,
	'state': text
}
Res:
{
	'id': text
	'name': text,
	'description': text,
	'state': text,
}

/rooms/{roomid} (DELETE)
-> deletes room with roomid from path adn returns deleted room
Res:
{
	'id': text
	'name': text,
	'description': text,
	'state': text,
}


##puzzles

/puzzles (GET)
->Returns all puzzles with devices
Res:
{'puzzles': [{
				'id': text,
				'name': text,
				'description': text,
				'state': text,
				'room': text,
				'devices':[{
					'id': text,
					'devType': text,
					'state': text,
					'puzzle': text,
				},...]
			},...]
}

/puzzles/{puzzleid} (GET)
->returns puzzle for puzzleid in path
Res:
{
	'id': text
	'name': text,
	'description': text,
	'state': text,
	'room': text,
}

/puzzles (POST)
->adds puzzle from request and returns the new puzzle
Req:
{
	'name': text,
	'description': text,
	'state': text,
	'room': text,
}

Res:
{
	'id': text
	'name': text,
	'description': text,
	'state': text,
	'room': text,
}

/puzzles/{puzzleid} (PUT)
->updates puzzle for puzzleid from path returns the updated puzzle
Req:
{

	'name': text,
	'description': text,
	'state': text,
	'room': text
}
Res:
{
	'id': text
	'name': text,
	'description': text,
	'state': text,
	'room': text,
}

/puzzles/{puzzleid} (DELETE)
-> deletes puzzle with roomid from path and returns deleted puzzle
Res{
	'id': text
	'name': text,
	'description': text,
	'state': text,
	'room': text,
}


##devices

/devices (GET)
->returns all devices
{'devices': [{
				'id': text,
				'devType': text,
				'state': text,
				'puzzle': text,
			},...]
}

/devices/{deviceid} (GET)
->returns device for deviceid in path
Res:
{
	'id': text
	'devType': text,
	'description': text,
	'state': text,
	'puzzle': text
}

/devices/{deviceid} (PUT)
->updates device for deviceid from path and returns the updated device
Req:
{
	'state': text,
	'puzzle': text
}
Res:
{
	'id': text
	'name': text,
	'state': text,
	'puzzle': text
}

/devices/{deviceid} (DELETE)
-> deletes device with deviceid from path and returns deleted device
Res{
	'id': text
	'devType': text,
	'state': text,
	'puzzle': text
}


Questions: 
ID for PUT in request or path? -> currently path: path -> identification, request -> updatable
Always return all puzzles/devices for room/puzzle requests? -> currently not
DELETE Device -> Remove Device also from RD?
PUT device -> What makes sense here? -> Only Update puzzle or also state?


ToDo:

Getall rooms/puzzles/devices orm 
Device discovery -> get on device for observe
coap server should use orm
-> move orm classes into separete file
puzzle solve logic(romm solve logic) -> solved event?
default room/ default puzzle -> id=0  for new devices
presentation slides for 2nd milestone