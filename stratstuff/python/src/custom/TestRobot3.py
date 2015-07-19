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

        self.idlemode = IdleMode(adapter, self)
        self.setMode(0, self.idlemode)

    def setScopeFilter(self):
        groundlist = []
        elementlist = []
        objlist = []
        objlist.append(3)

        sfilter = MyScopeFilter()
        self.applyScopeFilter(sfilter)

    def signalReceived(self, message):
        print(message)

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

class IdleMode(Mode):
    def __init__(self, adapter, robot):
        Mode.__init__(self, adapter, robot)
        idlestate = IdleState(adapter, robot, self)
        self.setState(0, idlestate)

class IdleState(State):
    def __init__(self, adapter, robot, mode):
        State.__init__(self, adapter, robot, mode)
        self.adapter = adapter

    def execute(self):
        scope = self.robot.getScope()

        if len(scope.getObjects()) > 0:
            obj = scope.getObjects()[0]
            print str(self.adapter.getWorld().canPickUp(obj.getObjectType()))
            x = obj.getX()
            y = obj.getY()
            z = obj.getZ()
            self.robot.moveTo(x, y, z)
            self.robot.pickUpItem(obj.getObjectID())

        self.robot.wait(3000)
