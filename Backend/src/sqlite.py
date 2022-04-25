import sqlite3


def connect_to_db():
    conn = sqlite3.connect('./database.db')
    return conn


def create_db_table():
    try:
        conn = connect_to_db()
        conn.execute('''
            CREATE TABLE ledtest (
                ledid INTEGER PRIMARY KEY NOT NULL,
                led TEXT NOT NULL,
                value INTEGER NOT NULL
            );
        ''')

        conn.commit()
        print("ledtest table created successfully")
    except:
        print("ledtest table creation failed - Maybe table")
    finally:
        conn.close()


def insert_led(led):
    inserted_led = {}
    try:
        conn = connect_to_db()
        cur = conn.cursor()
        cur.execute("INSERT INTO ledtest (led, value) VALUES ('{}', 0)".format(led))
        conn.commit()
        inserted_led = get_led_by_name(led)
    except:
        conn().rollback()

    finally:
        conn.close()
    return inserted_led


def update_led(led):
    updated_led = {}
    try:
        conn = connect_to_db()
        cur = conn.cursor()
        cur.execute("UPDATE ledtest SET value = {} WHERE led = '{}'".format(led['value'], led['name']))
        conn.commit()
        updated_led = get_led_by_name(led['name'])
    except:
        conn().rollback()

    finally:
        conn.close()
    return updated_led


def get_led_value(led):
    value = 0
    try:
        conn = connect_to_db()
        cur = conn.cursor()
        cur.execute("SELECT value from ledtest where led = '{}'".format(led))
        row = cur.fetchone()
        value = row[0]
    except:
        conn().rollback()

    finally:
        conn.close()
    return value


def get_led_by_name(led):
    fetched_led = {}
    try:
        conn = connect_to_db()
        cur = conn.cursor()
        cur.execute("SELECT * from ledtest where led = '{}'".format(led))
        row = cur.fetchone()
        fetched_led["name"] = row[1]
        fetched_led["value"] = row[2]
    except:
        conn().rollback()

    finally:
        conn.close()
    return (fetched_led)
