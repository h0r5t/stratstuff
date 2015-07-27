from random import randint


from src.API.Robot import Robot

from src.API.ScopeFilter import ScopeFilter
from src.engine.Scope import Scope


class TestRobot(Robot):
    def __init__(self, adapter, m_objectID):
        Robot.__init__(self, adapter, m_objectID)
        self.setScopeFilter()

    def setScopeFilter(self):
        sfilter = MyScopeFilter()
        self.applyScopeFilter(sfilter)

    def signalReceived(self, message):
        pass

    #ContextCommand
    def move(self, worldPoint):
        self.moveTo(worldPoint.getX(), worldPoint.getY(), worldPoint.getZ())

    #ContextCommand
    def shoot(self, worldPoint):
        self.turn(worldPoint.getX(), worldPoint.getY(), worldPoint.getZ())
        self.fire()

    #ContextCommand
    def contextTest3(self, worldPoint):
        pass

    #ContextCommand
    def contextTest4(self, worldPoint):
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
