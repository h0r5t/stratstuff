from threading import Thread
import time
from src.engine.Scope import Scope

SLEEP_TIME = 0.2

class Robot(Thread):
    def __init__(self, adapter, m_objectID):
        Thread.__init__(self)
        self.adapter = adapter
        self.objectID = m_objectID  
        self.modes = {}     
        self.currentModeIndex = 0
        self.visionScope = None
        
    def run(self):
        self.execute()
    
    def update(self):
        pass
    
    def getObjectID(self):
        return self.objectID
    
    def execute(self):
        self.modes[self.currentModeIndex].execute()
    
    def setMode(self, index, mode):
        self.modes[index] = mode
        
    def watchEvent(self, event):
        while 1:
            if event.wasCalledBack():
                return event.getMessage()
            time.sleep(SLEEP_TIME)
                
        
    # commands for the robot
    
    def moveTo(self, x, y, z):
        # returns true if unit could successfully move to target, false if not
        event = self.adapter.registerMoveTask(self.objectID, x, y, z)
        msg = self.watchEvent(event)
        return bool(msg)
    
    def getScope(self):
        # returns everything the robot can see as a Scope object
        event = self.adapter.registerGetScope(self.objectID)
        msg = self.watchEvent(event)
        scope = Scope(msg)
        return scope
    
    def turn(self, x, y, z):
        # turns the roboter until he faces the target point, returns true if successful
        event = self.adapter.registerObjectTurn(self.objectID, x, y, z)
        msg = self.watchEvent(event)
        return bool(msg)
    
    def fire(self):
        # robot fires in front
        self.adapter.registerFire(self.objectID)
        self.wait(200)
        
    def wait(self, millis):
        # waits the amount of millis and calls back after
        event = self.adapter.registerIdleTask(millis)
        msg = self.watchEvent(event)
        return msg
    
    