from threading import Thread
import time

SLEEP_TIME = 0.2

class Robot(Thread):
    def __init__(self, adapter, m_objectID):
        Thread.__init__(self)
        self.adapter = adapter
        self.objectID = m_objectID  
        self.modes = {}     
        self.currentModeIndex = 0
        
    def run(self):
        self.execute()
    
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
    
    def move(self, x, y, z):
        # returns true if unit could successfully movet to target, false if not
        event = self.adapter.registerMoveTask(self.objectID, x, y, z)
        msg = self.watchEvent(event)
        return bool(msg)
    
    def getScope(self):
        # returns everything the robot can see
        pass
