from src.engine.Scope import Scope


class ScopeFilter():

    def __init__(self, accepted_ground_list, accepted_element_list, accepted_obj_list):
        self.accepted_ground_list = accepted_ground_list
        self.accepted_element_list = accepted_element_list
        self.accepted_obj_list = accepted_obj_list

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
