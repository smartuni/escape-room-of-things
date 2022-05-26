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



# JSON Object representations:

## Room:
{
	'id': text
	'name': text,
	'description': text,
	'state': text,
}

## Puzzle:
{
	'id': text
	'name': text,
	'description': text,
	'state': text,
	'room': text,
}

## Device:
{ 
  'id': text, 
  'devIP': text, 
  'name': text, 
  'description': text,
  'state': text, 
  'puzzle': text
}

# Discovery of devices

There will be a default room with id=0 and a default puzzle with id=0 in which we will store devices which want to integrated in our IoT network.
The FRONTEND can get them with a call to /puzzles (GET)and filter for id=0 on puzzle



# api

/path
-> description
Req:{Request}
Res:{Response}

## misc

/rooms/state/{roomid} (PUT) 
-> admin puts a specific room into a new state (states either "ready" or "maintainance")
Req. Payload: {'state': text}

Res:
{
	'id': text
	'name': text,
	'description': text,
	'state': text
}

/puzzles/state/{puzzleid} (PUT) 
-> admin puts a specific puzzle into a new state (states either "ready" or "maintainance")
Req. Payload: {'state': text}

Res:
{
	'id': text
	'name': text,
	'description': text,
	'state': text,
	'room': text
}

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
						'devIP': text, 
						'name': text, 
						'description': text,
						'state': text, 
						'puzzle': text
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
	'state': text
}

/rooms (POST)
->adds room from request and returns the new room
Req. Payload:
{
	'name': text,
	'description': text
}

Res:
{
	'id': text
	'name': text,
	'description': text,
	'state': text
}

/rooms/{roomid} (PUT)
->updates room for roomid from path and returns the updated room
Req. Payload:
{
	'name': text,
	'description': text
}

Res:
{
	'id': text
	'name': text,
	'description': text,
	'state': text
}

/rooms/{roomid} (DELETE)
-> deletes room with roomid from path adn returns deleted room
Res:
{
	'id': text
	'name': text,
	'description': text,
	'state': text
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
					'devIP': text, 
					'name': text, 
					'description': text,
					'state': text, 
					'puzzle': text
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
	'room': text
}

/puzzles (POST)
->adds puzzle from request and returns the new puzzle
Req. Payload:
{
	'name': text,
	'description': text,
	'room': text
}

Res:
{
	'id': text
	'name': text,
	'description': text,
	'state': text,
	'room': text
}

/puzzles/{puzzleid} (PUT)
->updates puzzle for puzzleid from path returns the updated puzzle
Req. Payload:
{
	'name': text,
	'description': text,
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
				'devIP': text, 
				'name': text, 
				'description': text,
				'state': text, 
				'puzzle': text		
			},...]
}

/devices/{deviceid} (GET)
->returns device for deviceid in path
Res:
{
	'id': text, 
  	'devIP': text, 
  	'name': text, 
  	'description': text,
  	'state': text, 
  	'puzzle': text
}

/devices/{deviceid} (PUT)
->updates puzzle of device for specified deviceid and returns the updated device
Req. Payload:
{
	'puzzle': text
}

Res:
{
	'id': text, 
  	'devIP': text, 
  	'name': text, 
  	'description': text,
  	'state': text, 
  	'puzzle': text
}

/devices/{deviceid} (DELETE)
-> deletes device with deviceid from path and returns deleted device
Res{
	'id': text, 
  	'devIP': text, 
  	'name': text, 
  	'description': text,
  	'state': text, 
  	'puzzle': text
}


Questions: 
ID for PUT in request or path? -> currently path: path -> identification, request -> updatable
Always return all puzzles/devices for room/puzzle requests? -> currently not
DELETE Device -> Remove Device also from RD?


ToDo:
Device discovery -> get on device for observe
coap server should use orm
puzzle solve logic(romm solve logic) -> solved event?
presentation slides for 2nd milestone

Done:
Getall rooms/puzzles/devices orm
move orm classes into separete file
default room/ default puzzle -> id=0  for new devices