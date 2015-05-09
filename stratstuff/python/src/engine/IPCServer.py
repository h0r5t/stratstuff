import SocketServer
import thread

callbackFunction = None

class IPCServerHandler(SocketServer.BaseRequestHandler):
    def handle(self):
        data = self.request[0].strip()
        list_of_lines = data.split('\n');
        callbackFunction(list_of_lines)

def start(makeBarrier):
    global callbackFunction
    callbackFunction = makeBarrier
    server = SocketServer.UDPServer(("localhost", 32001), IPCServerHandler)
    thread.start_new_thread(server.serve_forever, ())  # @UndefinedVariable
