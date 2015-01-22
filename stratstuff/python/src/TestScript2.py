from Scripts import IDynamicScript

class TestScript2(IDynamicScript):
    def customInit(self):
        self.x = 30
        self.unit = self.adapter.getWorld().getObjectByID(144002)
        self.moveUnit()
        
    def update(self):
        pass
    
    def moveUnit(self):
        self.taskID = self.adapter.registerMoveTask(144002, self.x, 14, 0, self.idle)
        if self.x == 30:
            self.x = 10
        else:
            self.x = 30
    
    def idle(self):
        self.taskID = self.adapter.registerIdleTask(4000, self.moveUnit)
