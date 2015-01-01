import SocketServer
from thread import start_new_thread

callbackFunction = None

class IPCServerHandler(SocketServer.BaseRequestHandler):
    def handle(self):
        data = self.request[0].strip()
        list_of_lines = data.split('\n');
        callbackFunction(list_of_lines)

def start(callback):
    global callbackFunction
    callbackFunction = callback
    server = SocketServer.UDPServer(("localhost", 32001), IPCServerHandler)
    start_new_thread(server.serve_forever, ())
