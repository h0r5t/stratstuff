import ArtInt
import InfoReader
from Scripts import IDynamicScript
import Units
import WorldData


class ItemManager(IDynamicScript):
    def customInit(self):
        pass
    
    def update(self):
        pass


class UnitManager(IDynamicScript):
    def customInit(self):
        self.objectData = self.loadObjectData()
        self.workerObjects = self.adapter.getWorld().getObjectsWithType(0)
        self.workers = []
        self.createWorkerUnits()
    
    def update(self):
        for worker in self.workers:
            worker.update()
        
    def createWorkerUnits(self):
        for obj in self.workerObjects:
            worker = Units.Unit(obj)
            ai = ArtInt.MoveTestAI(self.adapter, obj)
            worker.setAI(ai)
            self.workers.append(worker)
    
    def loadObjectData(self):
        f = WorldData.dataDir + "/objects.info"
        return InfoReader.readFile(f)
