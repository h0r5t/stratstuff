import Tasks

class AI:
    def __init__(self, adapter, m_object):
        self.taskQueue = Tasks.TaskQueue()
        self.m_object = m_object
        self.adapter = adapter
        
    def isIdle(self):
        return self.taskQueue.isIdle()
    
    def getObject(self):
        return self.m_object
    
    def update(self):
        self.taskQueue.update()

class SubscriberAI(AI):
    # AI for testing
    def __init__(self, adapter, m_object):
        AI.__init__(self, adapter, m_object)
        self.taskdepot = adapter.getTaskDepot()  # instance of TaskDepot
    
    def update(self):
        AI.update(self)
        
        if self.taskQueue.isIdle():
            task = self.taskdepot.getOpenTask(self)
            if task != None:
                task.setM_Object(self.m_object)
                self.taskQueue.queueTask(task)
    
class MoveTestAI(AI):
    # AI for testing
    def __init__(self, adapter, m_object):
        AI.__init__(self, adapter, m_object)
        task1 = Tasks.MoveTask(adapter, self.m_object, 30, 14, 0)
        task2 = Tasks.MoveTask(adapter, self.m_object, 15, 15, 1)
        task3 = Tasks.MoveTask(adapter, self.m_object, 5, 20, 0)
        idletask = Tasks.IdleTask(adapter, self.m_object, 4000)
        
        self.taskQueue.queueTask(task1)
        self.taskQueue.queueTask(idletask)
        self.taskQueue.queueTask(task2)
        self.taskQueue.queueTask(idletask)
        self.taskQueue.queueTask(task3)
    
    def update(self):
        AI.update(self)
        # normally do stuff here
    
