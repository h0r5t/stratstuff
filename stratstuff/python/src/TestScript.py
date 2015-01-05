from Scripts import IDynamicScript

class TestScript(IDynamicScript):
    def customInit(self):
        self.x = 20
        self.removeBarrier()
        
        self.adapter.registerMoveTask(0, self.x, 10 , 0)
        
        self.adapter.registerEventTaskFinished(0, self.callback2)
        self.adapter.registerLocalEvent(self.statement, self.makeBarrier)
        
    def update(self):
        pass
            
    def makeBarrier(self):
        self.adapter.registerGroundChange(1, 12, 10, 0)
        self.adapter.registerLocalEvent(self.statement, self.makeBarrier)
    
    def removeBarrier(self):
        self.adapter.registerGroundChange(0, 12, 10, 0)
    
    def callback2(self):
        self.removeBarrier()
        self.adapter.registerMoveTask(0, self.x, 10 , 0)
        self.adapter.registerEventTaskFinished(0, self.callback2)
        
        if self.x == 20:
            self.x = 7
        else:
            self.x = 20
        
    def statement(self):
        if self.adapter.getWorld().getObjectByIndexInList(0).getX() == 13:
            return True
        
        return False
        
