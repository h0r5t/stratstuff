
class Script():
    def __init__(self):
        pass
        
class IDynamicScript(Script):
    
    def __init__(self, adapterCallback):
        self.adapter = adapterCallback
        self.waitingForEventCallback = False;
        self.customInit()
        
    def customInit(self):
        raise NotImplementedError("Should have implemented this")
        
    def update(self):
        raise NotImplementedError("Should have implemented this")
    
    def pleaseUpdate(self):
        if self.waitingForEventCallback == True:
            return False
        else:
            return True
    
    def eventOccurred(self, event):
        self.waitingForEventCallback = False;
        m = event.getMethod()
        m()
        
    def waitForEventCallback(self):
        self.waitingForEventCallback = True

class IStaticScript(Script):

    def update(self):
        raise NotImplementedError("Should have implemented this")
