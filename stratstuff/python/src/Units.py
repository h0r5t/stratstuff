class Unit:
    def __init__(self, m_object):
        # the underlying MovingObject for this unit
        self.m_object = m_object
        self.inventory = []
        self.ai = None
        
    def setAI(self, ai):
        self.ai = ai
    
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
        self.ai.update()