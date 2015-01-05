from Scripts import IDynamicScript

class ItemManager(IDynamicScript):
    def customInit(self):
        self.counter = 0
        self.booleanValue = False
        pass
    
    def update(self):
        self.counter = self.counter + 1
        if self.counter == 40:
            self.adapter.registerSetPaintObject(144001, not self.booleanValue)
            self.counter = 0
            self.booleanValue = not self.booleanValue
