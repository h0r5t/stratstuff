from EngineAdapter import EngineAdapterClass

class Script():
    def __init__(self):
        pass
        
class IDynamicScript(Script):
    
    def __init__(self, adapterCallback):
        self.adapter = adapterCallback
        self.customInit()
        
    def customInit(self):
        raise NotImplementedError("Should have implemented this")
        
    def update(self):
        raise NotImplementedError("Should have implemented this")
    
    def eventOccurred(self, event):
        raise NotImplementedError("Should have implemented this")
        

class IStaticScript(Script):

    def execute(self):
        raise NotImplementedError("Should have implemented this")
    
class Event():
    def __init__(self, ID):
        self.id = ID
    
    def getID(self):
        return self.id
        
        
