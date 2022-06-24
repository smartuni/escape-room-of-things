import sqlite3


def connect_to_db():
    conn = sqlite3.connect('./database.db')
    return conn


# Rooms

def create_table_room():
    try:
        conn = connect_to_db()
        conn.execute('''
            CREATE TABLE Rooms (
                id INTEGER PRIMARY KEY NOT NULL,
                name TEXT UNIQUE NOT NULL,
                state TEXT
            );
        ''')

        conn.commit()
        print("rooms table created successfully")
    except:
        print("rooms table creation failed - Maybe table")
    finally:
        conn.close()


def add_room(room):
    inserted_room = {}
    try:
        conn = connect_to_db()
        cur = conn.cursor()
        cur.execute("INSERT INTO rooms (name) VALUES ('{}')".format(room))
        conn.commit()
        inserted_room = get_room_by_name(room)
    except:
        conn().rollback()

    finally:
        conn.close()
    return inserted_room


def delete_room(room):
    deleted_room = {}
    try:
        conn = connect_to_db()
        cur = conn.cursor()
        deleted_room = get_room_by_name(room)
        cur.execute("DELETE from rooms where name = ('{}')".format(room))
        conn.commit()
    except:
        conn().rollback()

    finally:
        conn.close()
    return deleted_room


def update_room(state, room):
    updated_room = {}
    try:
        conn = connect_to_db()
        cur = conn.cursor()
        cur.execute("UPDATE rooms SET state = {} WHERE name = '{}' ".format(state,room))
        conn.commit()
        updated_room = get_room_by_name(room)
    except:
        conn().rollback()

    finally:
        conn.close()
    return updated_room


def get_room_by_name(room):
    fetched_room = {}
    try:
        conn = connect_to_db()
        cur = conn.cursor()
        cur.execute("SELECT * from rooms where name = '{}'".format(room))
        row = cur.fetchone()
        fetched_room["name"] = row[1]
        fetched_room["state"] = row[2]
    except:
        conn().rollback()

    finally:
        conn.close()
    return (fetched_room)


# Puzzles

def create_table_puzzle():
    try:
        conn = connect_to_db()
        conn.execute('''
            CREATE TABLE Puzzles (
                id INTEGER PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                path TEXT NOT NULL,
                room TEXT NOT NULL,
                state TEXT,
                FOREIGN KEY(room) REFERENCES name(rooms)
            );
        ''')

        conn.commit()
        print("puzzles table created successfully")
    except:
        print("puzzles table creation failed - Maybe table")
    finally:
        conn.close()


def add_puzzle(puzzlename, path):
    inserted_puzzle = {}
    try:
        conn = connect_to_db()
        cur = conn.cursor()
        cur.execute("INSERT INTO puzzles (name, path, room, state) VALUES ('{}','{}','Default','Open')".format(puzzlename,path))
        conn.commit()
        inserted_puzzle = get_puzzle_by_name(puzzlename)
    except:
        conn().rollback()

    finally:
        conn.close()
    return inserted_puzzle


def delete_puzzle(puzzle):
    deleted_puzzle = {}
    try:
        conn = connect_to_db()
        cur = conn.cursor()
        deleted_puzzle = get_puzzle_by_name(puzzle)
        cur.execute("DELETE from puzzles where name = ('{}')".format(puzzle))
        conn.commit()
    except:
        conn().rollback()

    finally:
        conn.close()
    return deleted_puzzle


def update_puzzle(newRoom, oldRoom, puzzle):
    updated_puzzle = {}
    try:
        conn = connect_to_db()
        cur = conn.cursor()
        cur.execute("UPDATE puzzles SET room = {} WHERE name = '{}' and room = '{}'".format(newRoom, puzzle, oldRoom))
        conn.commit()
        updated_puzzle = get_room_by_name(puzzle['name'])
    except:
        conn().rollback()

    finally:
        conn.close()
    return updated_puzzle


def get_puzzle_by_name(puzzle):
    fetched_puzzle = {}
    try:
        conn = connect_to_db()
        cur = conn.cursor()
        cur.execute("SELECT * from puzzles where name = '{}'".format(puzzle))
        row = cur.fetchone()
        fetched_puzzle["name"] = row[1]
        fetched_puzzle["path"] = row[2]
        fetched_puzzle["room"] = row[3]
        fetched_puzzle["state"] = row[4]
    except:
        conn().rollback()

    finally:
        conn.close()
    return (fetched_puzzle)


def get_puzzle_by_name_and_room(puzzle, room):
    fetched_puzzle = {}
    try:
        conn = connect_to_db()
        cur = conn.cursor()
        cur.execute("SELECT * from puzzles where name = '{}' and room = '{}'".format(puzzle, room))
        row = cur.fetchone()
        fetched_puzzle["name"] = row[1]
        fetched_puzzle["path"] = row[2]
        fetched_puzzle["room"] = row[3]
        fetched_puzzle["state"] = row[4]
    except:
        conn().rollback()

    finally:
        conn.close()
    return (fetched_puzzle)


def get_rooms_with_puzzles():
    rooms = {}

    try:
        conn = connect_to_db()
        cur = conn.cursor()
        cursor = cur.execute("SELECT * from puzzles")
        for row in cursor:
            if row[3] not in rooms:
                rooms[row[3]] = []
            rooms[row[3]].append(row[1])
    except:
        conn().rollback()

    finally:
        conn.close()
    return (rooms)
