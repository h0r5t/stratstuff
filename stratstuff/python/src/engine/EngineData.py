import numpy
from random import randint

import InfoReader


worldsDir = "../../resources/worlds"
dataDir = "../../resources/data"

class EngineData():
    def __init__(self, world_name):
        self.worldName = world_name
        self.wp_array = None  # will be initialized via numpy notation
        self.m_objects = []  # movingObjects
        self.groundData = {}  # ground data, will be loaded
        self.elementData = {}  # element data, will be loaded
        self.objectData = {}  # object data, will be loaded
        
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
        intID = int(objID)
        for o in self.m_objects:
            if o.getObjectID() == intID:
                o.setX(int(newX))
                o.setY(int(newY))
                o.setZ(int(newZ))
        
    def addMovingObject(self, objID, objType, x, y, z):
        foundList = [a for a in self.m_objects if a.getObjectID() == objID]
        if len(foundList) == 0:
            obj = MovingObject(objType, objID, x, y, z)
            self.m_objects.append(obj)
            
    def removeMovingObject(self, uniqueID):
        foundList = [a for a in self.m_objects if a.getObjectID() == uniqueID]
        for obj in foundList:
            self.m_objects.remove(obj)
        
    # ------------------ World changes ---------------------
     
    def groundCollides(self, groundID):
        if groundID == -1:
            return False
        a = self.groundData[str(groundID)]
        return "true" == a["collides"]
    
    def getObjects(self):
        return self.m_objects
    
    def getObjectsWithType(self, typeID):
        sublist = []
        for obj in self.m_objects:
            if str(obj.getObjectType()) == str(typeID):
                sublist.append(obj)
                
        return sublist
    
    def getObjectByID(self, theID):
        for obj in self.m_objects:
            if int(obj.getObjectID()) == int(theID):
                return obj
            
    def getAvailableObjectID(self):
        templist = []
        for obj in self.m_objects:
            templist.append(int(obj.getObjectID()))
        while 1:
            random = randint(0, len(self.m_objects) * 2)
        
            if random not in templist:
                return random
    
    def elementCollides(self, elementID):
        if elementID == -1:
            return False
        a = self.elementData[str(elementID)]
        return "true" == a["collides"]
    
    def getNoncollidingPositionNearWP(self, wp):
        x = int(wp.getX())
        y = int(wp.getY())
        z = int(wp.getZ())
        
        #testen ob out of world!!!!
        
        
        if not self.worldPointCollides(x + 1, y, z):
            return self.wp_array[x + 1, y, z]
        
        if x - 1 > 0:
            if not self.worldPointCollides(x - 1, y, z):
                return self.wp_array[x - 1, y, z]
        
        if not self.worldPointCollides(x, y + 1, z):
            return self.wp_array[x, y + 1, z]
        
        if y - 1 > 0:
            if not self.worldPointCollides(x, y - 1, z):
                return self.wp_array[x, y - 1, z]
        
        return None
    
    def getAllWPsInRange(self, x, y, z, w, h, d):
        wps = []
        for xx in range(x, x + w):
            for yy in range(y, y + h):
                for zz in range(z, z + d):
                    wps.append(self.wp_array[xx, yy, zz])
                
        return wps
    
    def getObjectsAt(self, x, y, z):
        objs = []
        for obj in self.m_objects:
            if obj.getX() == x and obj.getY() == y and obj.getZ() == z:
                objs.append(obj)
                
        return objs
    
    def worldPointCollides(self, x, y, z):
        wp = self.wp_array[x, y, z]        
        
        if self.groundCollides(wp.getGroundID()) or self.elementCollides(wp.getElementID()):
            return True
        
        return False
    
    def getElementIDByName(self, name):
        for item in self.elementData.items():
            if item[1]["name"] == name:
                return item[0]
            
    def getGroundIDByName(self, name):
        for item in self.groundData.items():
            if item[1]["name"] == name:
                return item[0]
            
    def getObjectIDByName(self, name):
        for item in self.objectData.items():
            if item[1]["name"] == name:
                return item[0]
            
    def getElementData(self):
        return self.elementData
    
    def getGroundData(self):
        return self.groundData
    
    def getObjectData(self):
        return self.objectData
    
    def loadFromFilesystem(self):
        # self.loadGroundIDs()
        # self.loadElements()
        # self.loadObjects()
        self.loadGroundData()
        self.loadElementData()
        self.loadObjectData()
        
    def loadGroundIDs(self):
        self.wp_array = numpy.empty((120, 120, 10), dtype=object)  # @UndefinedVariable
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
                    wp = WorldPoint(ground_id, x, y, z)
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
            
            object_type = int(split[0])
            objID = int(split[1])
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
        
    def loadObjectData(self):
        f = dataDir + "/objects.info"
        self.objectData = InfoReader.readFile(f)
                    
class WorldPoint():
    def __init__(self, ground_id, x, y, z):
        self.groundID = ground_id
        self.x = int(x)
        self.y = int(y)
        self.z = int(z)
        self.element = -1;
    
    def setElementID(self, element):
        self.element = element
        
    def getElementID(self):
        return self.element
    
    def getGroundID(self):
        return self.groundID
    
    def setGroundID(self, newgroundID):
        self.groundID = newgroundID
    
    def getX(self):
        return self.x
    
    def getY(self):
        return self.y
    
    def getZ(self):
        return self.z
    
    def setX(self, x):
        self.x = x
    
    def setY(self, y):
        self.y = y
        
    def setZ(self, z):
        self.z = z

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
        

class Item():
    def __init__(self, itemID, itemType, linkedObject, ownerObjID, infoText):
        self.itemID = itemID
        self.itemType = itemType
        self.ownerObjID = ownerObjID
        self.linkedObj = linkedObject  # -1 when no linked object exists
        self.infoText = infoText
        self.pickupTaskCreated = False
        self.droptaskCreated = False
    
    def getItemID(self):
        return self.itemID
    
    def setOwnerObjID(self, objID):
        self.ownerObjID = objID
    
    def getOwnerObjID(self):
        return self.ownerObjID
    
    def setPickUpTaskCreated(self, value):
        self.pickupTaskCreated = value
    
    def pickUpTaskCreated(self):
        return self.pickupTaskCreated
    
    def setDropTaskCreated(self, value):
        self.droptaskCreated = value
    
    def getDropTaskCreated(self):
        return self.droptaskCreated
    
    def getItemType(self):
        return self.itemType
        
    def setLinkedObjectUniqueID(self, objID):
        self.linkedObj = int(objID)
    
    def getLinkedObjectUniqueID(self):
        return self.linkedObj
    
    def getInfoText(self):
        return self.infoText
        
class Unit:
    def __init__(self, m_object):
        self.m_object = m_object
        self.inventory = []
            
    def getObject(self):
        return self.m_object
    
    def isIdle(self):
        return self.ai.isIdle()
    
    def getInventory(self):
        return self.inventory
    
    def addItemToInventory(self, item):
        self.inventory.append(item)
        item.setOwnerObjID(self.m_object.getObjectID())
        
    def removeItemFromInventory(self, item):
        self.inventory.remove(item)
        item.setOwnerObjID(-1)
        
    def update(self):
        pass

    
