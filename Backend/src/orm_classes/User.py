from .shared import db


class User(db.Model):
    id = db.Column(db.INTEGER, primary_key=True)
    public_id = db.Column(db.String(50), unique=True)
    username = db.Column(db.String(50), nullable=False, unique=True)
    password = db.Column(db.String(100), nullable=False)
    admin = db.Column(db.BOOLEAN)

    def __repr__(self):
        return f'ID: {self.id}\nPublic_id: {self.public_id}\nUsername: {self.username}\nPassword: {self.password}\nAdmin: {self.admin}'

    def serialize(self):
        """Return object data in easily serializable format"""
        return {
            'id': self.id,
            'public_id': self.public_id,
            'username': self.username,
            'password': self.password,
            'admin': self.admin
        }
