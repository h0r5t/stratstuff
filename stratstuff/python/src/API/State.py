class State():

    def __init__(self, adapter, robot, mode):
        self.mode = mode
        self.adapter = adapter
        self.robot = robot
    
    def execute(self):
        raise NotImplementedError
        
