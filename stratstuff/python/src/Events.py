    
class LocalEvent():
    def __init__(self, ID, statementMethod, callbackMethod):
        self.ID = ID
        self.method = callbackMethod
        self.statement_method = statementMethod
    
    def evaluate(self):
        boolVal = self.statement_method()
        return boolVal
    
    def callback(self):
        self.method()
    
    def getID(self):
        return self.ID
    
class RemoteEvent():
    def __init__(self, ID, callbackMethod):
        self.method = callbackMethod
        self.ID = ID
        
    def getID(self):
        return self.ID
        
    def callback(self, datastring):
        if datastring == None:
            self.method()
        else:   
            self.method(datastring)