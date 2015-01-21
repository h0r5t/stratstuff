from WorldData import WorldPoint

class InputManager:
    def __init__(self, adapter):
        self.adapter = adapter
        # do some init stuff
  
    def handleInput(self, message):
        # user entered some command via the engine interface, handle these
    
    
        self.split = message.split(" ")
        self.inputIDString = self.split[0]
        self.selection = None
        self.areaX = 0
        self.areaY = 0
        self.areaZ = 0
        self.areaW = 0
        self.areaH = 0
        self.areaD = 0
    
        if len(self.split) > 2:
            # we have some selection data that specifies where to do the task
            # x,y,z,w,h,d
            self.selection = self.split[2].split(",")
            self.areaX = int(self.selection[0])
            self.areaY = int(self.selection[1])
            self.areaZ = int(self.selection[2])
            self.areaW = int(self.selection[3])
            self.areaH = int(self.selection[4])
            self.areaD = int(self.selection[5])
            
        print self.inputIDString
        print self.selection
    
        if self.inputIDString == "input::chopTrees":
            pass
        
        elif self.inputIDString == "input::mine":
            wps = self.adapter.getWorld().getAllWPsInRange(self.areaX, self.areaY, self.areaZ, self.areaW, self.areaH, self.areaD)
            
            for wp in wps:
                print str(wp.getX()) + " " + str(wp.getY()) + " " + str(wp.getZ())
