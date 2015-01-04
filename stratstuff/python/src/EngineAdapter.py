import sys
import time

import IPCClient
import IPCServer
import TestScript
from WorldData import WorldData

client = IPCClient.IPCClient()
SLEEP_TIME = 0.5

class EngineAdapterClass:
    def __init__(self):
        self.scripts = []
        self.messages = []
        self.world = WorldData("test")
        
        self.setupScripts()
    
    def setupScripts(self):
        s1 = TestScript.TestScript(self)
        self.addScript(s1)
    
    def messageReceived(self, stringlist):
        print "received:"
        for s in stringlist:
            print s
            self.parseEngineMessage(s)
            
            
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
                
            time.sleep(SLEEP_TIME)
            
    def sendMessages(self):
        message = ""
        for msg in self.messages:
            message = message + msg + "\n";
              
        self.messages = []
        client.send(message);
        
    def addScript(self, script):
        self.scripts.append(script)
        
    # ----------------- Engine Messages ---------------
    
    def parseEngineMessage(self, messageString):
        split = messageString.split()
        messageID = split[0]
        
        if messageID == "0":
            x = split[len(split) - 3]
            y = split[len(split) - 2]
            z = split[len(split) - 1]
            newground = split[1]
            self.world.groundChanged(newground, x, y, z)
            
        if messageID == "1":
            x = split[len(split) - 3]
            y = split[len(split) - 2]
            z = split[len(split) - 1]
            movingObjectID = split[1]
            self.world.movingObjectPositionChanged(movingObjectID, x, y, z)
    
    # ----------------- Engine Messages ----------------
    
        
    # ----------------- Commands -----------------------
    
    def registerGroundChange(self, newGround, x, y, z):
        self.messages.append("chg " + str(newGround) + " " + str(x) + " " + str(y) + " " + str(z))
        self.world.groundChanged(newGround, x, y, z)
    
    
    # ----------------- Commands -----------------------

if __name__ == '__main__':
    
    adapter = EngineAdapterClass()
    
    print("starting...")
    IPCServer.start(adapter.messageReceived)
    print("server started.")
    
    adapter.loop()
