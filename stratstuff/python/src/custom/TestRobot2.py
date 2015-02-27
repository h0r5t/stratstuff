from src.API.Mode import Mode
from src.API.Robot import Robot
from src.API.State import State


class TestRobot2(Robot):
    def __init__(self, adapter, m_objectID):
        Robot.__init__(self, adapter, m_objectID)
        
        testmode = TestMode(None, self)
        self.setMode(0, testmode)
    
class TestMode(Mode):
    def __init__(self, adapter, robot):
        Mode.__init__(self, adapter, robot)
        teststate = MoveState1(adapter, robot, self)
        self.setState(0, teststate)
        teststate = MoveState2(adapter, robot, self)
        self.setState(1, teststate)
        
class MoveState1(State):
    def __init__(self, adapter, robot, mode):
        State.__init__(self, adapter, robot, mode)
        
    def execute(self):
        self.robot.moveTo(10, 10, 0)
        self.robot.mine(25, 22, 0)
        
class MoveState2(State):
    def __init__(self, adapter, robot, mode):
        State.__init__(self, adapter, robot, mode)
        
    def execute(self):
        self.robot.moveTo(10, 25, 0)
        self.robot.mine(21, 23, 0)