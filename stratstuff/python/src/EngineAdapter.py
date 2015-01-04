import sys
import time

from Events import LocalEvent, RemoteEvent
import IPCClient
import IPCServer
import TestScript
from WorldData import WorldData


client = IPCClient.IPCClient()
SLEEP_TIME = 0.05

class EngineAdapterClass:
    def __init__(self):
        self.scripts = []
        self.messages = []
        self.remoteEvents = {}  # events that happen in the engine
        self.localEvents = {}  # events that will be evaluated in frontend
        self.world = WorldData("test")
        
        self.setupScripts()
    
    def setupScripts(self):
        s1 = TestScript.TestScript(self)
        self.addScript(s1)
    
    def messageReceived(self, stringlist):
        for s in stringlist:
            self.parseEngineMessage(s)
            
    def registerLocalEvent(self, statementMethod, callbackMethod):
        eventID = self.getSmallesAvailableyKey(self.localEvents)
        event = LocalEvent(eventID, statementMethod, callbackMethod)
        self.localEvents[eventID] = event
        
    def loop(self):
        while 1:
            self.updateLocalEvents()
            self.updateScripts()
            self.sendMessages()
                
            time.sleep(SLEEP_TIME)
    
    def updateScripts(self):
        for script in self.scripts:
                try:
                    if script.pleaseUpdate():
                        script.update()
                except:
                    # this will avoid the adapter from crashing if script
                    # contains any errors
                    print "Unexpected error:", sys.exc_info()[0]
                    
    def updateLocalEvents(self):
        for event in self.localEvents.values():
            if event.evaluate():
                event.callback()
                
    def sendMessages(self):
        message = ""
        for msg in self.messages:
            message = message + msg + "\n";
              
        self.messages = []
        client.send(message);
        
    def addScript(self, script):
        self.scripts.append(script)
    
    def getWorld(self):
        return self.world
        
    def getSmallesAvailableyKey(self, dictionary):
        for key in dictionary:
            intkey = int(key)
            if not intkey + 1 in dictionary:
                return intkey + 1
        
        # empty list
        return 0
        
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
        
        if messageID == "2":
            eventID = int(split[1])
            event = self.remoteEvents[eventID]
            del self.remoteEvents[eventID]
            event.callback()
            
        if messageID == "3":
            x = split[len(split) - 3]
            y = split[len(split) - 2]
            z = split[len(split) - 1]
            newelement = split[1]
            self.world.elementChanged(newelement, x, y, z)
            print split
    
    # ----------------- Engine Messages ----------------
    
        
    # ----------------- Commands -----------------------
    
    def registerGroundChange(self, newGround, x, y, z):
        self.messages.append("chg " + str(newGround) + " " + str(x) + " " + str(y) + " " + str(z))
        self.world.groundChanged(newGround, x, y, z)
        
    def registerMoveTask(self, unitID, x, y, z):
        self.messages.append("move " + str(unitID) + " " + str(x) + " " + str(y) + " " + str(z))
    
    def registerEventTaskFinished(self, taskID, callbackMethod):
        eventID = self.getSmallesAvailableyKey(self.remoteEvents)
        event = RemoteEvent(eventID, callbackMethod)
        self.remoteEvents[eventID] = event
        self.messages.append("event " + str(0) + " " + str(eventID) + " " + str(taskID))
    
    # ----------------- Commands -----------------------

if __name__ == '__main__':
    
    adapter = EngineAdapterClass()
    
    print("starting...")
    IPCServer.start(adapter.messageReceived)
    print("server started.")
    
    adapter.loop()
