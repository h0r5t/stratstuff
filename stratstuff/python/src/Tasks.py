import EngineAdapter

class TaskQueue():
    def __init__(self):
        self.queue = []
        self.calledback = True
        
    def queueTask(self, task):
        # task has to be of type Task
        self.queue.append(task)
        
    def update(self):
        if self.calledback:
            self.calledback = False
            if len(self.queue) > 0:
                nextTask = self.queue.pop(0)
                nextTask.execute(self.callback)
        
    def callback(self):
        self.calledback = True
        
class Task():
    def __init__(self, adapter, m_object):
        self.adapter = adapter
        self.m_object = m_object
            
    def execute(self, callback):
        # execute task and if done call the callback function
        raise NotImplementedError("Should have implemented this")
    
class MoveTask(Task):
    def __init__(self, adapter, m_object, x, y, z):
        Task.__init__(self, adapter, m_object)
        self.x = x
        self.y = y
        self.z = z
        
    def execute(self, callback):
        self.adapter.registerMoveTask(self.m_object.getObjectID(), self.x, self.y, self.z, callback)
        