import time

import IPCClient
import IPCServer
import TestScript


client = IPCClient.IPCClient()

class EngineAdapterClass:
    def __init__(self):
        self.scripts = []
        self.messages = []
    
    def setupScripts(self):
        s1 = TestScript.TestScript(self)
        self.addScript(s1)
    
    def messageReceived(self, stringlist):
        print "received:"
        for s in stringlist:
            print s
            
    def registerEvent(self, event):
        # IMPLEMENT
        pass
        
    def loop(self):
        while 1:
            for script in self.scripts:
                try:
                    if script.pleaseUpdate():
                        script.update()
                except:
                    # this will avoid the adapter from crashing if script
                    # contains any errors
                    print "Unexpected error:", sys.exc_info()[0]
                
            self.sendMessages()
                
            time.sleep(0.5)
            
    def sendMessages(self):
        message = ""
        for msg in self.messages:
            message = message + msg + "\n";
              
        self.messages = []
        client.send(message);
        print "sent " + message
        
    def addScript(self, script):
        self.scripts.append(script)
        
    # ----------------- Commands -----------------------
    
    def registerGroundChange(self, newGround, x, y, z):
        self.messages.append("chg " + str(newGround) + " " + str(x) + " " + str(y) + " " + str(z))
    
    
    # ----------------- Commands -----------------------

if __name__ == '__main__':
    
    adapter = EngineAdapterClass()
    adapter.setupScripts()
    
    print("starting...")
    IPCServer.start(adapter.messageReceived)
    print("server started.")
    
    adapter.loop()
