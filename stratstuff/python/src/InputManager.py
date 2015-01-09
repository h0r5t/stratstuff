class InputManager:
  def __init__(self):
    #do some init stuff
  
  def handleInput(self, message):
    #user entered some command via the engine interface, handle these
    
    #--------------- here it comes --------------------
    
    split = message.split(" ")
    selection = None
    inputIDString = split[0]
    
    if split.length > 1:
      #we have some selection data that specifies where to do the task
      selection = split[1]
    
    if inputIDString == "input::chopTrees"
      #do some stuff for the selection (some x,y,z bounds for where to do cuttrees...
    
    #--------------- here it comes --------------------
