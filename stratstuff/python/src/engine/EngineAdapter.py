from random import randint
import sys
import time

sys.path.append('..')

import EngineData
from Events import RemoteEvent
import IPCClient
import IPCServer
from src.custom import TestRobot


client = IPCClient.IPCClient()
SLEEP_TIME = 0.05

class EngineAdapterClass:
    def __init__(self):
        self.scripts = []
        self.messages = []
        self.remoteEvents = {}  # events that happen in the engine
        self.world = EngineData.EngineData("test")
        self.locked = True
        
        self.setupRobots()
        
    
    def messageReceived(self, stringlist):
        for s in stringlist:
            self.parseEngineMessage(s)
        
    def loop(self):
        while 1:
            if self.locked == False:
                self.sendMessages()
                
                self.lock()
            
            time.sleep(SLEEP_TIME)
            
    def setupRobots(self):
        # test
        r1 = TestRobot.TestRobot(self, 144003)
        r1.start()
                
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
    
    def getTaskDepot(self):
        return self.taskdepot
        
    def getSmallestAvailableyKey(self, dictionary):      
        while 1:
            random = randint(0, len(dictionary) * 2)
        
            if random not in dictionary:
                return random
        
    # ----------------- Engine Messages ---------------
    
    def parseEngineMessage(self, messageString):
        split = messageString.split()
        messageID = split[0]
        
        if messageID.startswith("input::"):
            # it's an actual game feature, the user wants to do smth
            # the whole string should only look like: "input::dosomedefinedtask"
            pass
            
        elif messageID == "SHUTDOWN":
            self.doShutdown()
        
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
            if len(split) == 3:
                datastring = str(split[2])
                event.setCalledBack(datastring)
            else:
                event.setCalledBack("")
            
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
    
    def registerElementChange(self, newElement, x, y, z):
        self.messages.append("che " + str(newElement) + " " + str(x) + " " + str(y) + " " + str(z))
        self.world.elementChanged(newElement, x, y, z)
    
    def registerMoveTask(self, objectID, x, y, z):
        eventID = self.getSmallestAvailableyKey(self.remoteEvents)
        event = RemoteEvent(eventID)
        self.remoteEvents[eventID] = event
        self.messages.append("move " + str(objectID) + " " + str(x) + " " + str(y) + " " + str(z) + " " + str(eventID))
        return event
        
    def registerObjectSpawn(self, unitType, newObjID, x, y, z):
        self.messages.append("spawn " + str(unitType) + " " + str(newObjID) + " " + str(x) + " " + str(y) + " " + str(z))
        self.world.addMovingObject(newObjID, unitType, x, y, z)
        
    def registerObjectRemoval(self, uniqueObjID):
        self.messages.append("remObj " + str(uniqueObjID))
        self.world.removeMovingObject(uniqueObjID)
     
    def registerSetPaintObject(self, unitID, boolVal):
        self.messages.append("paintObj " + str(unitID) + " " + str(boolVal))
      
    def registerIdleTask(self, millis):
        eventID = self.getSmallestAvailableyKey(self.remoteEvents)
        event = RemoteEvent(eventID)
        self.remoteEvents[eventID] = event
        self.messages.append("idletask " + str(millis) + " " + str(eventID))
        return event
    
    def registerDisplayInfo(self, infoString):
        self.messages.append("dispInfo " + str(infoString))
        
    def registerGetScope(self, objID):
        eventID = self.getSmallestAvailableyKey(self.remoteEvents)
        event = RemoteEvent(eventID)
        self.remoteEvents[eventID] = event
        self.messages.append("getScope " + str(objID) + " " + str(eventID))
        return event
    
    # ----------------- Commands -----------------------
    
    def doShutdown(self):
        pass
    
if __name__ == '__main__':
    adapter = EngineAdapterClass()
    
    print(">>> starting...")
    IPCServer.start(adapter.messageReceived)
    print(">>> server started.")
    adapter.loop()