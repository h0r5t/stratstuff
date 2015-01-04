import numpy

worldsDir = "/home/h0r5t/code/git/stratstuff/resources/worlds"


class WorldData():
    def __init__(self, world_name):
        self.worldName = world_name
        self.wp_array = None  # will be initialized via numpy notation
        self.m_objects = []  # am array of WorldPoints
        self.loadFromFilesystem()
        
    # ------------------ World changes ---------------------
    
    def groundChanged(self, newgroundID, x, y, z):
        self.wp_array[x, y, z].setGroundID(newgroundID)
        
    def movingObjectPositionChanged(self, objID, newX, newY, newZ):
        for o in self.m_objects:
            if o.getObjectID() == objID:
                o.setX(newX)
                o.setY(newY)
                o.setZ(newZ)
                print "changed" 
                # FALSCH DAS IST DER TYPE NICHT DIE ID!!!!!!!
        
     # ------------------ World changes ---------------------
    
    def loadFromFilesystem(self):
        self.loadGroundIDs()
        self.loadElements()
        self.loadObjects()
        
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
            x = split[len(split) - 3]
            y = split[len(split) - 2]
            z = split[len(split) - 1]
            
            self.wp_array[x, y, z].setElementID(element_id)
    
    def loadObjects(self):
        f = open(worldsDir + "/" + self.worldName + "/objects.txt", "r");
        for line in f:
            line = line.rstrip("\n")
            split = line.split()
            
            object_id = split[0]
            x = split[len(split) - 3]
            y = split[len(split) - 2]
            z = split[len(split) - 1]
            
            obj = MovingObject(object_id, x, y, z)
            
            self.m_objects.append(obj)
                    
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
    
    def collides(self):
        # TODO via ground attributes JSON File, 
        # also checks if element collides
        return False

class MovingObject():
    def __init__(self, obj_id, x, y, z):
        self.obj_id = obj_id
        self.x = x
        self.y = y
        self.z = z
    
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
