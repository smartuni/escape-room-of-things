from .shared import db


class Device(db.Model):
    id = db.Column(db.INTEGER, primary_key=True)
    devIP = db.Column(db.String(100), unique=False, nullable=False)
    name = db.Column(db.String(20), unique=False, nullable=False)
    description = db.Column(db.String(1000), unique=False, nullable=True)
    state = db.Column(db.String(20), unique=False, nullable=False)
    puzzle = db.Column(db.INTEGER, db.ForeignKey('puzzle.id'), nullable=False)

    def __repr__(self):
        return 'ID: {}\nDevice IP: {}\nName: {}\nState: {}\nPuzzle: {}'.format(self.id, self.devIP, self.name,
                                                                                      self.state, self.puzzle)

    def serialize(self):
        """Return object data in easily serializable format"""
        return {
            'id': self.id,
            'devIP': self.devIP,
            'name': self.name,
            'description': self.description,
            'state': self.state,
            'puzzle': self.puzzle
        }