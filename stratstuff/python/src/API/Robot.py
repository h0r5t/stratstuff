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
        self.scopeFilter = None
        self.inventory = [] # TODO

    def run(self):
        self.execute()

    def destroy(self):
        self.modes[self.currentModeIndex].destroyMode()

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

    # settings for the robot

    def applyScopeFilter(self, scopeFilter):
        self.scopeFilter = scopeFilter

    # interface methods for the robot

    def signalReceived(self, message):
        pass

    # user commands for the robot

    def moveTo(self, x, y, z):
        # returns true if unit could successfully move to target, false if not
        event = self.adapter.registerMoveTask(self.objectID, x, y, z)
        if event == None:
            return
        msg = self.watchEvent(event)
        return bool(msg)

    def getScope(self):
        # returns everything the robot can see as a Scope object
        event = self.adapter.registerGetScope(self.objectID)
        if event == None:
            scope = Scope("")
            return scope
        msg = self.watchEvent(event)
        scope = Scope(msg)

        # check for filter
        if self.scopeFilter == None:
            return scope

        filteredScope = self.scopeFilter.parse(scope)
        return filteredScope

    def turn(self, x, y, z):
        # turns the roboter until he faces the target point, returns true if successful
        event = self.adapter.registerObjectTurn(self.objectID, x, y, z)
        if event == None:
            return
        msg = self.watchEvent(event)
        return bool(msg)

    def fire(self):
        # robot fires in front
        self.adapter.registerFire(self.objectID)
        self.wait(50)

    def wait(self, millis):
        # waits the amount of millis and calls back after
        event = self.adapter.registerIdleTask(self.objectID, millis)
        if event == None:
            return
        msg = self.watchEvent(event)
        return msg

    def mine(self, x, y, z):
        # mines the wp
        self.turn(x, y, z)
        event = self.adapter.registerMine(self.objectID, x, y, z)
        if event == None:
            return
        msg = self.watchEvent(event)
        return bool(msg)

    def pickUpItem(self, linkedObjUID):
        # robot picks up item (needs to stand on the wp the item is on)
        self.adapter.registerPickUpItem(self.objectID, linkedObjUID)
        self.wait(500)

    def sendRadialSignal(self, message):
        # sends a signal in all directions with the given message
        self.wait(500)
        self.adapter.registerRadialSignal(self.objectID, message)
        self.wait(500)
