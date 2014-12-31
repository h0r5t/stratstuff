import socket

class IPCClient():
    
    def __init__(self):
        self.HOST, self.PORT = "localhost", 32000
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    
    def send(self, data):
        self.sock.sendto(data + "\n" , (self.HOST, self.PORT))