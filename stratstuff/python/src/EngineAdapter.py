from random import randint
import sys
import time

from Events import LocalEvent, RemoteEvent
import IPCClient
import IPCServer
import Managers
import TestScript
import TestScript2
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
        self.locked = True
        self.inputMgr = InputManager.InputManager()
        
        self.setupScripts()
    
    def setupScripts(self):
        s1 = TestScript.TestScript(self)
        self.addScript(s1)
        
        itemMgr = Managers.ItemManager(self)
        self.addScript(itemMgr)
        s2 = TestScript2.TestScript2(self)
        self.addScript(s2)
    
    def messageReceived(self, stringlist):
        for s in stringlist:
            self.parseEngineMessage(s)
            
    def registerLocalEvent(self, statementMethod, callbackMethod):
        eventID = self.getSmallestAvailableyKey(self.localEvents)
        event = LocalEvent(eventID, statementMethod, callbackMethod)
        self.localEvents[eventID] = event
        
    def loop(self):
        while 1:
            if self.locked == False:
                self.updateLocalEvents()
                self.updateScripts()
                self.sendMessages()
                
                self.lock()
            
            time.sleep(0.01)
    
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
        for item in self.localEvents.items():
            event = item[1]
            if event.evaluate():
                event.callback()
                del self.localEvents[item[0]]
                
    def lock(self):
        self.locked = True
    
    def unlock(self):
        self.locked = False
                
    def sendMessages(self):
        message = ""
        for msg in self.messages:
            message = message + msg + "\n";
            
        message = message + "FIN\n"
              
        self.messages = []
        client.send(message);
        
    def addScript(self, script):
        self.scripts.append(script)
    
    def getWorld(self):
        return self.world
        
    def getSmallestAvailableyKey(self, dictionary):
        
        while 1:
            random = randint(0, len(dictionary) * 2)
        
            if random not in dictionary:
                return random
        
    # ----------------- Engine Messages ---------------
    
    def parseEngineMessage(self, messageString):
        split = messageString.split()
        messageID = split[0]
        
        if messageID.startswith("input::")
            # it's an actual game feature, the user wants to do smth
            # the whole string should only look like: "input::dosomedefinedtask"
            self.inputMgr.handleInput(messageString)
        
        elif messageID == "START":
            self.unlock()
        
        elif messageID == "0":
            x = split[len(split) - 3]
            y = split[len(split) - 2]
            z = split[len(split) - 1]
            newground = split[1]
            self.world.groundChanged(newground, x, y, z)
            
        elif messageID == "1":
            x = split[len(split) - 3]
            y = split[len(split) - 2]
            z = split[len(split) - 1]
            movingObjectID = split[1]
            self.world.movingObjectPositionChanged(movingObjectID, x, y, z)
        
        elif messageID == "2":
            eventID = int(split[1])
            event = self.remoteEvents[eventID]
            del self.remoteEvents[eventID]
            event.callback()
            
        elif messageID == "3":
            x = split[len(split) - 3]
            y = split[len(split) - 2]
            z = split[len(split) - 1]
            newelement = split[1]
            self.world.elementChanged(newelement, x, y, z)
            
        elif messageID == "4":
            x = split[len(split) - 3]
            y = split[len(split) - 2]
            z = split[len(split) - 1]
            objID = split[1]
            objType = split[2]
            self.world.addMovingObject(objID, objType, x, y, z)
    
    # ----------------- Engine Messages ----------------
    
        
    # ----------------- Commands -----------------------
    
    def registerGroundChange(self, newGround, x, y, z):
        self.messages.append("chg " + str(newGround) + " " + str(x) + " " + str(y) + " " + str(z))
        self.world.groundChanged(newGround, x, y, z)
        
    def registerMoveTask(self, unitID, x, y, z):
        self.messages.append("move " + str(unitID) + " " + str(x) + " " + str(y) + " " + str(z))
    
    def registerEventTaskFinished(self, taskID, callbackMethod):
        eventID = self.getSmallestAvailableyKey(self.remoteEvents)
        event = RemoteEvent(eventID, callbackMethod)
        self.remoteEvents[eventID] = event
        self.messages.append("event " + str(0) + " " + str(eventID) + " " + str(taskID))
        
    def registerUnitSpawn(self, unitType, x, y, z):
        self.messages.append("spawn " + str(unitType) + " " + str(x) + " " + str(y) + " " + str(z))
        
    def registerSetPaintObject(self, unitID, boolVal):
        self.messages.append("paintObj " + str(unitID) + " " + str(boolVal))
    
    # ----------------- Commands -----------------------

if __name__ == '__main__':
    adapter = EngineAdapterClass()
    
    print(">>> starting...")
    IPCServer.start(adapter.messageReceived)
    print(">>> server started.")
    adapter.loop()
