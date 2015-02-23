class RemoteEvent():
    def __init__(self, ID):
        self.ID = ID
        self.calledback = False
        self.callbackmsg = ""
        
    def getID(self):
        return self.ID
    
    def getMessage(self):
        return self.callbackmsg
    
    def setCalledBack(self, msg):
        self.calledback = True
        self.callbackmsg = msg
        
    def wasCalledBack(self):
        return self.calledback