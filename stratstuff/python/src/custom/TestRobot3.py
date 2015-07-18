from random import randint

from src.API.Mode import Mode
from src.API.Robot import Robot
from src.API.State import State

from src.engine.ScopeFilter import ScopeFilter


class TestRobot3(Robot):
    def __init__(self, adapter, m_objectID):
        Robot.__init__(self, adapter, m_objectID)

        self.setScopeFilter()

        self.idlemode = IdleMode(None, self)
        self.setMode(0, self.idlemode)

    def setScopeFilter(self):
        groundlist = []
        elementlist = []
        objlist = []

        objlist.append(3)

        sfilter = ScopeFilter(groundlist, elementlist, objlist)

        self.applyScopeFilter(sfilter)

    def signalReceived(self, message):
        print(message)

class IdleMode(Mode):
    def __init__(self, adapter, robot):
        Mode.__init__(self, adapter, robot)
        idlestate = IdleState(adapter, robot, self)
        self.setState(0, idlestate)

class IdleState(State):
    def __init__(self, adapter, robot, mode):
        State.__init__(self, adapter, robot, mode)

    def execute(self):
        scope = self.robot.getScope()

        for wp in scope.getWorldPoints():
            print str(wp)

        for obj in scope.getObjects():
            print str(obj.getObjectID())

        self.robot.wait(3000)
