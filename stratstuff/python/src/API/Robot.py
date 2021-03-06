from threading import Thread
import time

from src.engine.Scope import Scope


SLEEP_TIME = 0.2

class Robot(Thread):
    def __init__(self, adapter, m_objectID):
        Thread.__init__(self)
        self.adapter = adapter
        self.objectID = m_objectID
        self.currentModeIndex = 0
        self.visionScope = None
        self.scopeFilter = None
        self.inventory = [] # list of item objects
        self.contextCommandsQueue = [] # list of contextCommands

    def run(self):
        self.execute()

    def destroy(self):
        pass

    def update(self):
        pass

    def getObjectID(self):
        return self.objectID

    def queueContextCommand(self, methodName, worldPoint):
        l = [str(methodName), worldPoint]
        self.contextCommandsQueue.append(l)

    def execute(self):
        while 1:
            for l in self.contextCommandsQueue[:]:
                methodName = l[0]
                worldPoint = l[1]

                getattr(self, methodName)(worldPoint)
                self.contextCommandsQueue.pop(0)

            time.sleep(SLEEP_TIME)

    def addToInventory(self, item):
        self.inventory.append(item)

    def removeFromInventory(self, item):
        itemID = item.getItemID()
        for item in self.inventory[:]:
            if int(item.getItemID()) == int(itemID):
                self.inventory.remove(item)

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

    def getInventory(self):
        return self.inventory

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

    def placeItem(self, item):
        # robot places the item off his inventory
        self.adapter.registerPlaceItem(self.objectID, item)
        self.wait(500)

    def dropItem(self, item):
        # robot drops the item off his inventory to the ground
        self.adapter.registerDropItem(self.objectID, item)
        self.wait(500)

    def sendRadialSignal(self, message):
        # sends a signal in all directions with the given message
        self.wait(500)
        self.adapter.registerRadialSignal(self.objectID, message)
        self.wait(500)
