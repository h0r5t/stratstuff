from Scripts import IDynamicScript

class TestScript(IDynamicScript):
    def update(self):
        self.adapter.testPrint()
    def eventOccurred(self, event):
        pass