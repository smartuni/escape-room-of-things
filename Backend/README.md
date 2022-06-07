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
# install linkheader for rd
pip3 install linkheader

starting rest.py with arguments ( e.g: 1) now creates db and default room+puzzle, without argument it expects them to exist

# JSON Object representations:

## Room:
{
	'id': text
	'name': text,
	'description': text,
	'state': text
}

## Puzzle:
{
	'id': text
	'name': text,
	'description': text,
	'state': text,
	'isVictory': bool,
	'room': text
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



# API

/path
-> description
Req:{Request}
Res:{Response}
JSON fields in () are optional fields

## States

/rooms/state/{roomid} (PUT) 
-> admin puts a specific room into a new state (states either "ready" or "maintainance")
Req. Payload: {'state': text}

Res:
{
	'id': text
	'name': text,
	'description': text,
	'state': text,
	'puzzles':[...]
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
	'room': text,
	'devices':[...]
}

/devices/state/{deviceid} (PUT) 
-> admin puts a specific device into a new state (states either "ready" or "maintainance")
Req. Payload: {'state': text}

Res:
{
	'id': text, 
						'devIP': text, 
						'name': text,
						'serial': text 
						'description': text,
						'state': text, 
						'puzzle': text,
						'node_state': text,
						'is_event_device': bool,
						'pubkey': text
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
				'isVictory': bool,
				'devices':[{
						'id': text, 
						'devIP': text, 
						'name': text,
						'serial': text 
						'description': text,
						'state': text, 
						'puzzle': text,
						'node_state': text,
						'is_event_device': bool,
						'pubkey': text
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
	'state': text,
	'puzzles':[...]
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
	'state': text,
	'puzzles':[...]
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


## Puzzles

/puzzles (GET)
->Returns all puzzles with devices
Res:
{'puzzles': [{
				'id': text,
				'name': text,
				'description': text,
				'state': text,
				'room': text,
				'isVictory': bool,
				'devices':[{
					'id': text, 
					'devIP': text, 
					'name': text,
					'serial': text 
					'description': text,
					'state': text, 
					'puzzle': text,
					'node_state': text,
					'is_event_device': bool,
					'pubkey': text
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
    'isVictory': bool,
	'devices':[...]
}

/puzzles (POST)
->adds puzzle from request and returns the new puzzle
Req. Payload:
{
	'name': text,
	'description': text,
	'room': text,
	'isVictory': bool
}

Res:
{
	'id': text
	'name': text,
	'description': text,
	'state': text,
	'room': text,
	'isVictory': bool,
	'devices':[...]
}

/puzzles/{puzzleid} (PUT)
->updates puzzle for puzzleid from path returns the updated puzzle
Req. Payload:
{
	'name': text,
	'description': text,
	'room': text,
	'isVictory': bool
}

Res:
{
	'id': text
	'name': text,
	'description': text,
	'state': text,
	'room': text,
	'isVictory': bool,
	'devices':[...]
}

/puzzles/{puzzleid} (DELETE)
-> deletes puzzle with roomid from path and returns deleted puzzle
Res{
	'id': text
	'name': text,
	'description': text,
	'state': text,
	'room': text,
	'isVictory': bool
}


## Devices

/devices (GET)
->returns all devices
{'devices': [{
				'id': text, 
					'devIP': text, 
					'name': text,
					'serial': text 
					'description': text,
					'state': text, 
					'puzzle': text,
					'node_state': text,
					'is_event_device': bool,
					'pubkey': text		
			},...]
}

/devices/{deviceid} (GET)
->returns device for deviceid in path
Res:
{
	'id': text, 
	'devIP': text, 
	'name': text,
	'serial': text 
	'description': text,
	'state': text, 
	'puzzle': text,
	'node_state': text,
	'is_event_device': bool,
	'pubkey': text
}

/devices (POST)
->adds the new device
Req. Payload:
{ 
	'serial': text, 
	'pubkey': text,
	('name': text,)
	('description': text,) 
	('puzzle': text,) default:0
	('is_event_device': bool)
}

Res:
{
	'id': text, 
	'devIP': text, 
	'name': text,
	'serial': text 
	'description': text,
	'state': text, 
	'puzzle': text,
	'node_state': text,
	'is_event_device': bool,
	'pubkey': text
}

/devices/{deviceid} (PUT)
->updates puzzle of device for specified deviceid and returns the updated device
Req. Payload:
{
	'puzzle': text,
	('is_event_device': bool)
}

Res:
{
	'id': text, 
	'devIP': text, 
	'name': text,
	'serial': text 
	'description': text,
	'state': text, 
	'puzzle': text,
	'node_state': text,
	'is_event_device': bool,
	'pubkey': text
}

/devices/{deviceid} (DELETE)
-> deletes device with deviceid from path and returns deleted device
Res{
	'id': text, 
	'devIP': text, 
	'name': text,
	'serial': text 
	'description': text,
	'state': text, 
	'puzzle': text,
	'node_state': text,
	'is_event_device': bool,
	'pubkey': text
}



## ToDo:
- Admin Authentication(Flask-Auth.)
- implement a way to change the device states by the server (solved -> ready / * -> maintainance)
- parse device coap answer in device observe and trigger check_game_state
- decode pubkey(base64) from FE before saving in db?
- authenticate devices (pubkey - private key), on observe? on connect to rd?
-> key exchange with devices

## Done:
- Post Device(Name, Description, pubkey)
- Put Device(is_event_device)
- add serial column to device
- State change for devices(like room and puzzle)
- Delete Puzzles on room delete and move devices to default Puzzle
- Update API to send complete responses (Puzzle -> isVictory, Device -> is_event_device, pubkey, nodestate, Room -> puzzles) + README(API)
- Update rd-observe(instead add device/update device)
- Observe rd for devices(disconnected devices) 