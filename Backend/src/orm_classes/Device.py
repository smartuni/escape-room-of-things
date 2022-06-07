from .shared import db


class Device(db.Model):
    id = db.Column(db.INTEGER, primary_key=True)
    devIP = db.Column(db.String(100), unique=False, nullable=False)
    name = db.Column(db.String(20), unique=False, nullable=False)
    serial = db.Column(db.String(20), unique=True, nullable=False)
    description = db.Column(db.String(1000), unique=False, nullable=True)
    state = db.Column(db.String(20), unique=False, nullable=False)
    puzzle = db.Column(db.INTEGER, db.ForeignKey('puzzle.id'), nullable=False)
    pubkey = db.Column(db.String(100), unique=False, nullable=False)
    node_state = db.Column(db.String(20), unique=False, nullable=False)
    is_event_device = db.Column(db.BOOLEAN)

    def __repr__(self):
        return 'ID: {}\nDevice IP: {}\nName: {}\nState: {}\nPuzzle: {}'\
            .format(self.id, self.devIP, self.name, self.serial, self.description, self.state, self.puzzle, self.node_state, self.is_event_device)

    def serialize(self):
        """Return object data in easily serializable format"""
        return {
            'id': self.id,
            'devIP': self.devIP,
            'name': self.name,
            'serial': self.serial,
            'description': self.description,
            'state': self.state,
            'puzzle': self.puzzle,
            'node_state': self.node_state,
            'is_event_device': self.is_event_device
        }
