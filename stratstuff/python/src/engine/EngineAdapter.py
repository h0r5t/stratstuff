from pydoc import locate
from random import randint
import sys
import time

import EngineData
from Events import RemoteEvent
import IPCClient
import IPCServer


sys.path.append('..')



client = IPCClient.IPCClient()
SLEEP_TIME = 0.05

class EngineAdapterClass:
    def __init__(self):
        self.scripts = []
        self.messages = []
        self.remoteEvents = {}  # events that happen in the engine
        self.locked = True
        self.robotsMap = {}  # objID:robot
        self.world = EngineData.EngineData("test")

    def messageReceived(self, stringlist):
        for s in stringlist:
            self.parseEngineMessage(s)

    def loop(self):
        while 1:
            if self.locked == False:
                self.sendMessages()

                self.lock()

            time.sleep(SLEEP_TIME)

    def instantiateRobot(self, designName, objUID):
        # returns an instance!
        robot = locate("src.custom." + designName + "." + designName)(self, int(objUID))
        robot.start()
        self.robotsMap[str(objUID)] = robot
        print "robot instance created"

    def stopRobot(self, objUID):
        robot = self.robotsMap[str(objUID)]
        robot.destroy()
        del self.robotsMap[str(objUID)]

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
            # self.world.groundChanged(newground, x, y, z)

        elif messageID == "1":
            x = split[len(split) - 3]
            y = split[len(split) - 2]
            z = split[len(split) - 1]
            movingObjectID = split[1]
            # self.world.movingObjectPositionChanged(movingObjectID, x, y, z)

        elif messageID == "2":
            eventID = int(split[1])
            if not self.remoteEvents.has_key(eventID):
                return
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
            # self.world.elementChanged(newelement, x, y, z)

        elif messageID == "4":
            x = split[len(split) - 3]
            y = split[len(split) - 2]
            z = split[len(split) - 1]
            objID = split[1]
            objType = split[2]
            # self.world.addMovingObject(objID, objType, x, y, z)

        elif messageID == "5":
            # set design for robot
            designName = split[1]
            objUID = int(split[2])
            self.instantiateRobot(designName, objUID)

        elif messageID == "6":
            # stop robot
            objUID = int(split[1])
            self.stopRobot(objUID)

        elif messageID == "7":
            # signal received
            objUID = int(split[1])
            msg = split[2]
            if self.robotsMap.has_key(str(objUID)):
                self.robotsMap[str(objUID)].signalReceived(msg)


    # ----------------- Engine Messages ----------------




    # ----------------- Commands -----------------------

    def registerMoveTask(self, objectID, x, y, z):
        if self.robotsMap.has_key(str(objectID)) == False:
            return
        eventID = self.getSmallestAvailableyKey(self.remoteEvents)
        event = RemoteEvent(eventID)
        self.remoteEvents[eventID] = event
        self.messages.append("move " + str(objectID) + " " + str(x) + " " + str(y) + " " + str(z) + " " + str(eventID))
        return event

    def registerSetPaintObject(self, unitID, boolVal):
        self.messages.append("paintObj " + str(unitID) + " " + str(boolVal))

    def registerIdleTask(self, objectID, millis):
        if self.robotsMap.has_key(str(objectID)) == False:
            return
        eventID = self.getSmallestAvailableyKey(self.remoteEvents)
        event = RemoteEvent(eventID)
        self.remoteEvents[eventID] = event
        self.messages.append("idletask " + str(millis) + " " + str(eventID))
        return event

    def registerDisplayInfo(self, infoString):
        self.messages.append("dispInfo " + str(infoString))

    def registerGetScope(self, objID):
        if self.robotsMap.has_key(str(objID)) == False:
            return
        eventID = self.getSmallestAvailableyKey(self.remoteEvents)
        event = RemoteEvent(eventID)
        self.remoteEvents[eventID] = event
        self.messages.append("getScope " + str(objID) + " " + str(eventID))
        return event

    def registerObjectTurn(self, objectID, x, y, z):
        if self.robotsMap.has_key(str(objectID)) == False:
            return
        eventID = self.getSmallestAvailableyKey(self.remoteEvents)
        event = RemoteEvent(eventID)
        self.remoteEvents[eventID] = event
        self.messages.append("turn " + str(objectID) + " " + str(x) + " " + str(y) + " " + str(z) + " " + str(eventID))
        return event

    def registerFire(self, objectID):
        if self.robotsMap.has_key(str(objectID)) == False:
            return
        self.messages.append("fire " + str(objectID))

    def registerMine(self, objectID, x, y, z):
        if self.robotsMap.has_key(str(objectID)) == False:
            return
        eventID = self.getSmallestAvailableyKey(self.remoteEvents)
        event = RemoteEvent(eventID)
        self.remoteEvents[eventID] = event
        self.messages.append("mine " + str(objectID) + " " + str(x) + " " + str(y) + " " + str(z) + " " + str(eventID))
        return event

    def registerRadialSignal(self, objectID, message):
        if self.robotsMap.has_key(str(objectID)) == False:
            return
        self.messages.append("radialsignal " + str(objectID) + " " + str(message))

    # ----------------- Commands -----------------------

    def doShutdown(self):
        pass

if __name__ == '__main__':
    adapter = EngineAdapterClass()

    print(">>> starting...")
    IPCServer.start(adapter.messageReceived)
    print(">>> server started.")
    print(">>> stratstuff Frontend v1.0")
    adapter.loop()
