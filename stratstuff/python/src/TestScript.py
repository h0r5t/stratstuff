from Scripts import IDynamicScript

class TestScript(IDynamicScript):
    def customInit(self):
        self.counter = 0;
        
    def update(self):
        if self.counter == 0:
            self.counter = 1;
        else:
            self.counter = 0;
        self.adapter.registerGroundChange(self.counter, 20, 20, 0)
        self.adapter.registerGroundChange(self.counter, 20, 20, 0)
        
    def eventOccurred(self, event):
        pass
