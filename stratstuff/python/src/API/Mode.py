class Mode():

    def __init__(self, adapter, robot):
        self.robot = robot
        self.states = {}
        self.currentStateIndex = 0
        self.destroy = False
        
    def getRobot(self):
        return self.robot
    
    def destroyMode(self):
        self.destroy = True
    
    def execute(self):
        while self.destroy == False:
            self.states[self.currentStateIndex].execute()
            
            self.currentStateIndex += 1
            if self.currentStateIndex >= len(self.states):
                self.currentStateIndex = 0
    
    def setState(self, index, state):
        self.states[index] = state
        
