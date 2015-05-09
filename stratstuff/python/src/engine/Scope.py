from EngineData import WorldPoint
from EngineData import MovingObject

class Scope():
    def __init__(self, datastring):
        self.wps = []  # list of worldpoints
        self.objs = []  # list of objects
        self.decode(datastring)
        
    def decode(self, datastring):
        split = datastring.split("&")
        
        for ele in split:
            split2 = ele.split(":")
            if split2[0] == "wp":
                x = int(split2[1])
                y = int(split2[2])
                z = int(split2[3])
                groundID = int(split2[4])
                elementID = int(split2[5])
                wp = WorldPoint(groundID, x, y, z)
                wp.setElementID(elementID)
                self.wps.append(wp)
                
            elif split2[0] == "obj":
                uniqueID = int(split2[1])
                objType = int(split2[2])
                x = int(split2[3])
                y = int(split2[4])
                z = int(split2[5])
                obj = MovingObject(objType, uniqueID, x, y, z)
                self.objs.append(obj)
                
    def getWorldPoints(self):
        return self.wps
    
    def getObjects(self):
        return self.objs
                
                
