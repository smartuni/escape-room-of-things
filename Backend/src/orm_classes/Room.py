from .shared import db


class Room(db.Model):
    id = db.Column(db.INTEGER, primary_key=True)
    name = db.Column(db.String(100), unique=False, nullable=False)
    description = db.Column(db.String(1000), unique=False, nullable=True)
    state = db.Column(db.String(20), unique=False, nullable=False)
    puzzles = db.relationship('Puzzle', backref='puzzle', lazy=True)

    def __repr__(self):
        return 'ID: {}\nName: {}\nDescription: {}\nState: {}'.format(self.id, self.name, self.description, self.state)

    def serialize(self):
        """Return object data in easily serializable format"""
        return {
            'id': self.id,
            'name': self.name,
            'description': self.description,
            'state': self.state
        }
    