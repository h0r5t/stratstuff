import InfoReader
import Items
from Scripts import IDynamicScript
import WorldData


class ItemManager(IDynamicScript):
    def customInit(self):
        self.itemsDataLoc = WorldData.dataDir + "/items.info"
        self.itemData = InfoReader.readFile(self.itemsDataLoc)
        self.items = {}
        self.loadItems()
        
    def getAvailableItemID(self):
        return len(self.items.keys())
    
    def getItemsOnWP(self, wp):
        foundlist = []
        for item in self.items.values():
            if (int(item.getLinkedObjectUniqueID() != -1)):
                obj = self.adapter.getWorld().getObjectByID(item.getLinkedObjectUniqueID())
                if obj.getX() == wp.getX() and obj.getY() == wp.getY() and obj.getZ() == wp.getZ():
                    foundlist.append(item)
        return foundlist
        
    def addAndLinkItem(self, item, x, y, z):
        uniqueObjectID = self.adapter.getWorld().getAvailableObjectID()
        objectType = int(self.itemData[str(item.getItemType())]["object"])
        
        self.adapter.registerObjectSpawn(objectType, uniqueObjectID, x, y, z)
        
        item.setLinkedObjectUniqueID(uniqueObjectID)
        
        self.items[item.getItemID()] = item
    
    def addUnlinkedItem(self, item):
        self.items[item.getItemID()] = item
        
    def linkItem(self, item, x, y, z):
        item = self.items[item.getItemID()]
        uniqueObjectID = self.adapter.getWorld().getAvailableObjectID()
        objectType = int(self.itemData[str(item.getItemType())]["object"])
        
        self.adapter.registerObjectSpawn(objectType, uniqueObjectID, x, y, z)
        
        item.setLinkedObjectUniqueID(uniqueObjectID)
        
    def unlinkItem(self, item):
        item = self.items[item.getItemID()]
        self.adapter.registerObjectRemoval(item.getLinkedObjectUniqueID())
        item.setLinkedObjectUniqueID(-1)
        
    def removeItem(self, item):
        del self.items[str(item.getItemID())]             
        
    def getItemIDByName(self, name):
        for item in self.itemData.items():
            if item[1]["name"] == name:
                return int(item[0])
                        
    def loadItems(self):
        self.itemsSaveLoc = WorldData.worldsDir + "/test/items.txt"
        f = open(self.itemsSaveLoc, "r")
        
        # format: itemID objectID infoString (separated by &)
        
        for line in f:
            line = line.rstrip("\n")
            split = line.split(" ")
            
            itemID = int(split[0])
            itemType = int(split[1])
            linkedObjID = int(split[2])
            ownerObjID = int(split[3])
            infoString = split[4].replace("&", " ")
            item = Items.Item(itemID, itemType, linkedObjID, ownerObjID, infoString)
            if ownerObjID != -1:
                self.adapter.getUnitManager().getUnitByObjectID(ownerObjID).addItemToInventory(item)
            self.items[item.getItemID()] = item
    
    def update(self):
        pass
    
    def getItemData(self):
        return self.itemData
    
    def save(self):
        f = open(self.itemsSaveLoc, "w")
        for item in self.items.values():
            infotext = str(item.getInfoText().replace(" ", "&"))
            f.write(str(item.getItemID()) + " " + str(item.getItemType()) + " " + str(item.getLinkedObjectUniqueID()) + " " + str(item.getOwnerObjID()) + " " + infotext + "\n")
    
