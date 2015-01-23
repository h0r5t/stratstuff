class Unit:
    def __init__(self, m_object):
        # the underlying MovingObject for this unit
        self.m_object = m_object
        self.ai = None
        
    def setAI(self, ai):
        self.ai = ai
    
    def getObject(self):
        return self.m_object
        
    def update(self):
        self.ai.update()