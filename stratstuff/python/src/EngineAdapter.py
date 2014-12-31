import time
import IPCClient
import IPCServer
import TestScript

client = IPCClient.IPCClient()

class EngineAdapterClass:
    def __init__(self):
        self.scripts = []
        pass
    
    def setupScripts(self):
        s1 = TestScript.TestScript(self)
        self.addScript(s1)
    
    def messageReceived(self, stringlist):
        for s in stringlist:
            print s
        
    def loop(self):
        while 1:
            for script in self.scripts:
                script.update()
                
            time.sleep(1)

    def testPrint(self):
        print "hi from script"
        
    def addScript(self, script):
        self.scripts.append(script)

if __name__ == '__main__':
    
    adapter = EngineAdapterClass()
    adapter.setupScripts()
    
    print("starting...")
    IPCServer.start(adapter.messageReceived)
    print("server started.")
    
    adapter.loop()
