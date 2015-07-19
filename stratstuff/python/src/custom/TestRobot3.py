from random import randint

from src.API.Mode import Mode
from src.API.Robot import Robot
from src.API.State import State

from src.API.ScopeFilter import ScopeFilter
from src.engine.Scope import Scope


class TestRobot3(Robot):
    def __init__(self, adapter, m_objectID):
        Robot.__init__(self, adapter, m_objectID)
        self.setScopeFilter()

        self.testmode = TestMode(adapter, self)
        self.setMode(0, self.testmode)

    def setScopeFilter(self):
        groundlist = []
        elementlist = []
        objlist = []
        objlist.append(3)

        sfilter = MyScopeFilter()
        self.applyScopeFilter(sfilter)

    def signalReceived(self, message):
        pass

class MyScopeFilter(ScopeFilter):
    def __init__(self):
        self.accepted_ground_list = []
        self.accepted_element_list = []
        self.accepted_obj_list = [3]

    # parses a scope and returns a new Scope with only the wanted (accepted) stuff
    def parse(self, scope):
        scopeWPs = scope.getWorldPoints()
        scopeObjs = scope.getObjects()
        acceptedWPs = []
        acceptedObjs = []

        for wp in scopeWPs:
            if (wp.getGroundID() in self.accepted_ground_list) or (wp.getElementID() in self.accepted_element_list):
                acceptedWPs.append(wp)

        for obj in scopeObjs:
            if obj.getObjectType() in self.accepted_obj_list:
                acceptedObjs.append(obj)

        newScope = Scope("", acceptedWPs, acceptedObjs)
        return newScope

class TestMode(Mode):
    def __init__(self, adapter, robot):
        Mode.__init__(self, adapter, robot)
        state1 = PickUpState(adapter, robot, self)
        state2 = DropState(adapter, robot, self)
        self.setState(0, state1)
        self.setState(1, state2)

class PickUpState(State):
    def __init__(self, adapter, robot, mode):
        State.__init__(self, adapter, robot, mode)
        self.adapter = adapter

    def execute(self):
        scope = self.robot.getScope()

        for obj in scope.getObjects():
            x = obj.getX()
            y = obj.getY()
            z = obj.getZ()
            self.robot.moveTo(x, y, z)
            self.robot.pickUpItem(obj.getObjectID())

class DropState(State):
    def __init__(self, adapter, robot, mode):
        State.__init__(self, adapter, robot, mode)
        self.adapter = adapter

    def execute(self):

        for item in self.robot.getInventory()[:]:
            x = randint(0, 30)
            y = randint(0, 30)
            z = 0
            self.robot.moveTo(x, y, z)
            self.robot.dropItem(item)
