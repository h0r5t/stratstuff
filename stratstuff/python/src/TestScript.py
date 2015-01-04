from Scripts import IDynamicScript

class TestScript(IDynamicScript):
    def customInit(self):
        self.counter = 0;
        
    def update(self):
        pass
        
    def eventOccurred(self, event):
        pass
