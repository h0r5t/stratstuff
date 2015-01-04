import numpy
import InfoReader

worldsDir = "/home/h0r5t/code/git/stratstuff/resources/worlds"
dataDir = "/home/h0r5t/code/git/stratstuff/resources/data"

class WorldData():
    def __init__(self, world_name):
        self.worldName = world_name
        self.wp_array = None  # will be initialized via numpy notation
        self.m_objects = []  # movingObjects
        self.groundData = {}  # ground data, will be loaded
        self.elementData = {}  # element data, will be loaded
        self.loadFromFilesystem()
        
    # ------------------ World changes ---------------------
    
    def groundChanged(self, newgroundID, x, y, z):
        x = int(x)
        y = int(y)
        z = int(z)
        self.wp_array[x, y, z].setGroundID(newgroundID)
    
    def elementChanged(self, newElementID, x, y, z):
        x = int(x)
        y = int(y)
        z = int(z)
        self.wp_array[x, y, z].setElementID(newElementID)
        
    def movingObjectPositionChanged(self, objID, newX, newY, newZ):
        for o in self.m_objects:
            if o.getObjectID() == objID:
                o.setX(int(newX))
                o.setY(int(newY))
                o.setZ(int(newZ))
        
    # ------------------ World changes ---------------------
     
    def groundCollides(self, groundID):
        if groundID == -1:
            return False
        type = self.groundData[str(groundID)]
        return "true" == type["collides"]
    
    def getObjects(self):
        return self.m_objects
    
    def getObjectByID(self, theID):
        for obj in self.m_objects:
            if obj.getObjectID() == theID:
                return obj
    
    def getObjectByIndexInList(self, index):
        return self.m_objects[index]
    
    def elementCollides(self, elementID):
        if elementID == -1:
            return False
        type = self.elementData[str(elementID)]
        return "true" == type["collides"]
    
    def worldPointCollides(self, x, y, z):
        wp = self.wp_array[x, y, z]        
        
        if self.groundCollides(wp.getGroundID()) or self.elementCollides(wp.getElementID()):
            return True
        
        return False
    
    def loadFromFilesystem(self):
        self.loadGroundIDs()
        self.loadElements()
        self.loadObjects()
        self.loadGroundData()
        self.loadElementData()
        
    def loadGroundIDs(self):
        self.wp_array = numpy.empty((120, 120, 10), dtype=object)
        # numpy notation, for assignment use a[x, y, z], ez
    
        for z in range (0, 10):
            f = open(worldsDir + "/" + self.worldName + "/" + str(z) + ".layer", "r")
            
            x = 0;
            y = 0;
            for line in f:
                line = line.rstrip("\n")
                split = line.split()  # splits whitespaces per default
                for id_string in split:
                    ground_id = int(id_string)
                    wp = WorldPoint(ground_id)
                    self.wp_array[x, y, z] = wp
                    x = x + 1
                y = y + 1
                x = 0
            f.close()
    
    def loadElements(self):
        f = open(worldsDir + "/" + self.worldName + "/elements.txt", "r");
        for line in f:
            line = line.rstrip("\n")
            split = line.split()
            
            element_id = split[0]
            x = int(split[len(split) - 3])
            y = int(split[len(split) - 2])
            z = int(split[len(split) - 1])
            
            self.wp_array[x, y, z].setElementID(element_id)
    
    def loadObjects(self):
        f = open(worldsDir + "/" + self.worldName + "/objects.txt", "r");
        for line in f:
            line = line.rstrip("\n")
            split = line.split()
            
            object_type = split[0]
            objID = split[1]
            x = split[len(split) - 3]
            y = split[len(split) - 2]
            z = split[len(split) - 1]
            
            obj = MovingObject(object_type, objID, int(x), int(y), int(z))
            
            self.m_objects.append(obj)
            
    def loadGroundData(self):
        f = dataDir + "/grounds.info"
        self.groundData = InfoReader.readFile(f)
    
    def loadElementData(self):
        f = dataDir + "/elements.info"
        self.elementData = InfoReader.readFile(f)
                    
class WorldPoint():
    def __init__(self, ground_id):
        self.groundID = ground_id
        self.element = -1;
    
    def setElementID(self, element):
        self.element = element
        
    def getElementID(self):
        return self.element
    
    def getGroundID(self):
        return self.groundID
    
    def setGroundID(self, newgroundID):
        self.groundID = newgroundID

class MovingObject():
    def __init__(self, obj_type, obj_id, x, y, z):
        self.obj_type = obj_type
        self.obj_id = obj_id
        self.x = x
        self.y = y
        self.z = z
    
    def getObjectType(self):
        return self.obj_type
    
    def getObjectID(self):
        return self.obj_id
    
    def getX(self):
        return self.x
    
    def getY(self):
        return  self.y
    
    def getZ(self):
        return self.z
    
    def setX(self, x):
        self.x = x
    
    def setY(self, y):
        self.y = y
        
    def setZ(self, z):
        self.z = z