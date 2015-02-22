import ArtInt
import InfoReader
from Scripts import IDynamicScript
import Units
import WorldData


class UnitManager(IDynamicScript):
    def customInit(self):
        self.objectData = self.loadObjectData()
        self.workerObjects = self.adapter.getWorld().getObjectsWithType(0)
        self.workers = []
        self.units = []
        self.objectIDToUnitMap = {}
        self.createWorkerUnits()
    
    def update(self):
        for worker in self.workers:
            worker.update()
                        
    def addUnit(self, unit):
        self.units.append(unit)
        self.objectIDToUnitMap[str(unit.getObject().getObjectID())] = unit
    
    def getUnitByObjectID(self, objID):
        if self.objectIDToUnitMap.has_key(str(objID)):
            return self.objectIDToUnitMap[str(objID)]
        return None
    
    def getUnits(self):
        return self.units
                
    def createWorkerUnits(self):
        for obj in self.workerObjects:
            worker = Units.Unit(obj)
            ai = ArtInt.SubscriberAI(self.adapter, obj)
            worker.setAI(ai)
            self.workers.append(worker)
            self.addUnit(worker)
    
    def loadObjectData(self):
        f = WorldData.dataDir + "/objects.info"
        return InfoReader.readFile(f)
