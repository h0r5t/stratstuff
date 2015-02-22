import Items
from WorldData import WorldPoint


class TaskDepot(): 
    def __init__(self):
        self.opentasks = []
        
    def addOpenTask(self, task):
        self.opentasks.append(task)
        
    def cancelTask(self, task):
        self.opentasks.append(task)
                
    def getOpenTask(self, callee):
        if len(self.opentasks) > 0:
            task = self.opentasks[0]
            if task.hasFilter():
                if task.getFilter().getFilterType() == "filterbyobject":
                    if task.getFilter().check(callee.getObject().getObjectID()):
                        self.opentasks.remove(task)
                        return task
                    
            else:
                self.opentasks.remove(task)
                return task
            
        return None
        
class TaskQueue():
    def __init__(self):
        self.queue = []
        self.calledback = True
        
    def queueTask(self, task):
        # task has to be of type Task
        self.queue.append(task)
        
    def getSize(self):
        return len(self.queue)
        
    def isEmpty(self):
        if self.getSize() == 0:
            return True
        
        else:
            return False
        
    def update(self):
        if self.calledback:
            if len(self.queue) > 0:
                nextTask = self.queue.pop(0)
                self.calledback = False
                nextTask.execute(self.callback)
        
    def callback(self):
        self.calledback = True
        
    def isIdle(self):
        return self.calledback
    
class TaskFilter():
    def __init__(self):
        pass
    
    def check(self, objectOfInterest):
        # should return true if filter matches
        raise NotImplementedError("Should have implemented this")
    
class TaskFilterByObject(TaskFilter):
    def __init__(self, objectID):
        self.objectID = int(objectID)
        self.filtertype = "filterbyobject"
        
    def check(self, objectID):
        return self.objectID == int(objectID)
    
    def getFilterType(self):
        return self.filtertype
        
class Task():
    def __init__(self, adapter, m_object, prio):
        self.adapter = adapter
        self.m_object = m_object
        self.priority = prio  # higher = less important
        self.taskfilter = None
        
    def hasFilter(self):
        if self.taskfilter != None:
            return True
        return False
            
    def setFilter(self, taskfilter):
        self.taskfilter = taskfilter
        
    def getFilter(self):
        return self.taskfilter
        
    def setM_Object(self, m_object):
        self.m_object = m_object
            
    def execute(self, callback):
        # execute task and if done call the callback function
        raise NotImplementedError("Should have implemented this")
    
class MoveTask(Task):
    def __init__(self, adapter, m_object, prio, x, y, z):
        Task.__init__(self, adapter, m_object, prio)
        self.x = x
        self.y = y
        self.z = z
        self.callbackMethod = None
        
    def execute(self, callback):
        self.callbackMethod = callback
        self.adapter.registerMoveTask(self.m_object.getObjectID(), self.x, self.y, self.z, self.mycallback)
        
    def mycallback(self, datastring):
        self.callbackMethod()
        
class ChildMoveTask(Task):
    def __init__(self, adapter, m_object , prio, x, y, z):
        Task.__init__(self, adapter, m_object, prio)
        self.x = x
        self.y = y
        self.z = z
        self.callbackMethod = None
        
    def execute(self, callback):
        self.callbackMethod = callback
        self.adapter.registerMoveTask(self.m_object.getObjectID(), self.x, self.y, self.z, self.mycallback)
        
    def mycallback(self, datastring):
        self.callbackMethod(datastring)
    
class IdleTask(Task):
    def __init__(self, adapter, m_object, prio, durationmillis):
        Task.__init__(self, adapter, m_object, prio)
        self.durationmillis = durationmillis
        
    def execute(self, callback):
        self.adapter.registerIdleTask(self.durationmillis, callback)
        
class DesignationTask(Task):
    # choptrees, mining, etc.
    def __init__(self, adapter, prio, target_worldpoint):
        Task.__init__(self, adapter, None, prio)
        self.targetwp = target_worldpoint
        
    def execute(self, callback):
        self.callbackMethod = callback
        destwp = self.adapter.getWorld().getNoncollidingPositionNearWP(self.targetwp)
        if destwp == None:
            self.adapter.getTaskDepot().cancelTask(self)
            self.callbackMethod()
        else:
            self.adapter.registerMoveTask(self.m_object.getObjectID(), destwp.getX(), destwp.getY(), destwp.getZ(), self.designateIdle)
        
    def designateIdle(self, pathExists):
        pathexists = bool(pathExists)
        if pathexists:
            self.adapter.registerIdleTask(5000, self.designateFinish)
        else:
            self.adapter.getTaskDepot().cancelTask(self)
            self.callbackMethod()
        
    def designateFinish(self):
        itemType = int(self.adapter.getWorld().getElementData()[self.targetwp.getElementID()]["drops"])
        itemID = self.adapter.getItemManager().getAvailableItemID()
        item = Items.Item(itemID, itemType, -1, -1, "text to test")
        x = self.targetwp.getX()
        y = self.targetwp.getY()
        z = self.targetwp.getZ()
        self.adapter.getItemManager().addAndLinkItem(item, x, y, z)
        self.adapter.registerElementChange(-1, self.targetwp.getX(), self.targetwp.getY(), self.targetwp.getZ())
        self.callbackMethod()
        
class PickUpItemTask(Task):
    # pick up item from ground
    def __init__(self, adapter, prio, item):
        Task.__init__(self, adapter, None, prio)
        self.item = item
        
    def execute(self, callback):
        self.callbackMethod = callback
        self.itemObj = self.adapter.getWorld().getObjectByID(self.item.getLinkedObjectUniqueID())
        self.x = self.itemObj.getX()
        self.y = self.itemObj.getY()
        self.z = self.itemObj.getZ()
        moveTask = ChildMoveTask(self.adapter, self.m_object, 0, self.x, self.y, self.z)
        moveTask.execute(self.pickupItem)
        
    def pickupItem(self, pathExists):
        # no path can be found => pathExists = "False"
        pathexists = bool(pathExists)
        if pathexists:
            self.adapter.getItemManager().unlinkItem(self.item)
            unit = self.adapter.getUnitManager().getUnitByObjectID(self.m_object.getObjectID())
            unit.addItemToInventory(self.item)
            self.item.setPickUpTaskCreated(False)
            self.callbackMethod()
        else:
            self.adapter.getTaskDepot().cancelTask(self)
            self.item.setPickUpTaskCreated(False)
            self.callbackMethod()

class DropItemTask(Task):
    # drop item to ground
    def __init__(self, adapter, prio, item, x, y, z):
        Task.__init__(self, adapter, None, prio)
        self.item = item
        self.x = x
        self.y = y
        self.z = z
                
    def execute(self, callback):
        self.callbackMethod = callback
        self.itemOwnerObj = self.adapter.getWorld().getObjectByID(self.item.getOwnerObjID())
        moveTask = ChildMoveTask(self.adapter, self.m_object, 0, self.x, self.y, self.z)
        moveTask.execute(self.dropItem)
        
    def dropItem(self, pathExists):
        # no path can be found => pathExists = "False"
        pathexists = bool(pathExists)
        if pathexists:
            self.adapter.getItemManager().linkItem(self.item, self.x, self.y, self.z)
            unit = self.adapter.getUnitManager().getUnitByObjectID(self.item.getOwnerObjID())
            unit.removeItemFromInventory(self.item)
            self.item.setOwnerObjID(-1)
            self.item.setDropTaskCreated(False)
            self.callbackMethod()
        else:
            self.adapter.getTaskDepot().cancelTask(self)
            self.item.setDropTaskCreated(False)
            self.callbackMethod()
            
class BuildTask(Task):
    # build element at x y z and remove item
    def __init__(self, adapter, prio, item, elementID, x, y, z):
        Task.__init__(self, adapter, None, prio)
        self.item = item
        self.x = x
        self.y = y
        self.z = z