class Message:
    def __init__(self, role, message):
        self.role = role
        self.message = message

    def __str__(self):
        return "role: " + self.role + ", message: " + self.message + "|"
