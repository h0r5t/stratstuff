from random import randint

from src.API.Mode import Mode
from src.API.Robot import Robot
from src.API.State import State


class TestRobot3(Robot):
    def __init__(self, adapter, m_objectID):
        Robot.__init__(self, adapter, m_objectID)

        self.idlemode = IdleMode(None, self)
        self.setMode(0, self.idlemode)

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
        self.robot.wait(1000)
