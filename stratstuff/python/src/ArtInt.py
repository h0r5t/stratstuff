import Tasks

class AI:
    def __init__(self, adapter, m_object):
        self.taskQueue = Tasks.TaskQueue()
        self.m_object = m_object
        self.adapter = adapter
    
    def update(self):
        self.taskQueue.update()
    
class MoveTestAI(AI):
    def __init__(self, adapter, m_object):
        AI.__init__(self, adapter, m_object)
        task1 = Tasks.MoveTask(adapter, self.m_object, 30, 14, 0)
        task2 = Tasks.MoveTask(adapter, self.m_object, 15, 15, 1)
        task3 = Tasks.MoveTask(adapter, self.m_object, 5, 20, 0)
        self.taskQueue.queueTask(task1)
        self.taskQueue.queueTask(task2)
        self.taskQueue.queueTask(task3)
    
    def update(self):
        AI.update(self)
        # normally do stuff here
    