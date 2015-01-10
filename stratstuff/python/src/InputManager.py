class InputManager:
    def __init__(self):
        pass
        # do some init stuff
  
    def handleInput(self, message):
        # user entered some command via the engine interface, handle these
    
        #--------------- here it comes --------------------
    
        self.split = message.split(" ")
        self.selection = None
        self.inputIDString = self.split[0]
    
        if self.split.length > 1:
            # we have some selection data that specifies where to do the task
            self.selection = self.split[1]
    
        if self.inputIDString == "input::chopTrees":
            pass
            # do some stuff for the selection (some x,y,z bounds for where to do cuttrees...
    
            #--------------- here it comes --------------------
