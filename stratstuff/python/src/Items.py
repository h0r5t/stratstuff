
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
        
