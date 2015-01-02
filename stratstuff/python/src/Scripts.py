from EngineAdapter import EngineAdapterClass

class Script():
    def __init__(self):
        pass
        
class IDynamicScript(Script):
    
    def __init__(self, adapterCallback):
        self.adapter = adapterCallback
        self.customInit()
        self.waitingForEventCallback = False;
        
    def customInit(self):
        raise NotImplementedError("Should have implemented this")
        
    def update(self):
        raise NotImplementedError("Should have implemented this")
    
    def eventOccurred(self, event):
        self.waitingForEventCallback = False;
        m = event.getMethod()
        m()
        
    def registerEvent(self, event):
        self.adapter.registerEvent(event)
        self.waitingForEventCallback = True;

class IStaticScript(Script):

    def update(self):
        raise NotImplementedError("Should have implemented this")
    
class Event():
    def __init__(self, callbackMethod, ID):
        self.id = ID
        self.method = callbackMethod
    
    def getID(self):
        return self.id
        
    def getMethod(self):
        return self.method
