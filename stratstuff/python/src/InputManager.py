from Tasks import DesignationTask, MoveTask
import Tasks
from WorldData import WorldPoint


class InputManager:
    def __init__(self, adapter):
        self.adapter = adapter
        # do some init stuff
  
    def handleInput(self, message):
        self.world = self.adapter.getWorld()
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
    
        if self.split[1] == "area":
            # area selected
            self.selection = self.split[2].split(",")
            self.areaX = int(self.selection[0])
            self.areaY = int(self.selection[1])
            self.areaZ = int(self.selection[2])
            self.areaW = int(self.selection[3])
            self.areaH = int(self.selection[4])
            self.areaD = int(self.selection[5])
        
        elif self.split[1] == "single":
            # area selected
            self.selection = self.split[2].split(",")
            self.areaX = int(self.selection[0])
            self.areaY = int(self.selection[1])
            self.areaZ = int(self.selection[2])
                    
        if self.inputIDString == "input::move":
            movetask = MoveTask(self.adapter, None, 0, self.areaX, self.areaY, self.areaZ)
            self.adapter.getTaskDepot().addOpenTask(movetask)
    
        elif self.inputIDString == "input::trees":
            wps = self.world.getAllWPsInRange(self.areaX, self.areaY, self.areaZ, self.areaW, self.areaH, self.areaD)
            
            for wp in wps:
                if wp.getElementID() == self.world.getElementIDByName("tree"):
                    task = DesignationTask(self.adapter, 0, wp)
                    self.adapter.getTaskDepot().addOpenTask(task)
        
        elif self.inputIDString == "input::mine":
            wps = self.world.getAllWPsInRange(self.areaX, self.areaY, self.areaZ, self.areaW, self.areaH, self.areaD)
            
            for wp in wps:
                if wp.getElementID() != -1:
                    task = DesignationTask(self.adapter, 0, wp)
                    self.adapter.getTaskDepot().addOpenTask(task)
                    
        elif self.inputIDString == "input::collect_items":
            wps = self.world.getAllWPsInRange(self.areaX, self.areaY, self.areaZ, self.areaW, self.areaH, self.areaD)
            
            for wp in wps:
                for item in self.adapter.getItemManager().getItemsOnWP(wp):
                    if item.getLinkedObjectUniqueID() != -1 and item.pickUpTaskCreated() == False:
                        # item is on the map, will create a task to pick up
                        task = Tasks.PickUpItemTask(self.adapter, 5, item)
                        self.adapter.getTaskDepot().addOpenTask(task)
                        item.setPickUpTaskCreated(True)
        
        elif self.inputIDString == "input::display_inventory":
            objs = self.adapter.getWorld().getObjectsAt(self.areaX, self.areaY, self.areaZ)
            for obj in objs:
                unit = self.adapter.getUnitManager().getUnitByObjectID(obj.getObjectID())
                if unit != None:
                    infostring = "<title>Inventory&of&Unit<newline>"
                    for i in unit.getInventory():
                        itemName = self.adapter.getItemManager().getItemData()[str(i.getItemType())]["name"]
                        infostring = infostring + itemName + "<newline>"
                    self.adapter.registerDisplayInfo(infostring)

        elif self.inputIDString == "input::build_torch":
            torchID = self.adapter.getItemManager().getItemIDByName("torch")
            for unit in self.adapter.getUnitManager().getUnits():
                for item in unit.getInventory():
                    if item.getItemID() == torchID:
                        # this unit has a torch
                        unitObjID = unit.getObject().getObjectID()
                        lightElementID = self.adapter.getWorld().getElementIDByName("light")
                        buildtask = Tasks.BuildTask(self.adapter, 0, item, lightElementID, self.areaX, self.areaY, self.areaZ)
                        taskfilter = Tasks.TaskFilterByObject(unitObjID)
                        buildtask.setFilter(taskfilter)
                        self.adapter.getTaskDepot().addOpenTask(buildtask)
                        return
            
            infostring = "<title>No&unit&has&a&torch<newline>"
            self.adapter.registerDisplayInfo(infostring)
        
        elif self.inputIDString == "input::build_testlight":
            self.adapter.registerElementChange(8, self.areaX, self.areaY, self.areaZ)
            
        elif self.inputIDString == "input::build_nothingtest":
            self.adapter.registerElementChange(-1, self.areaX, self.areaY, self.areaZ)        

