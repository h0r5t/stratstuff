dataDir = "/home/h0r5t/code/git/stratstuff/resources/data"

# reads .info file and returns dictionary of dictionaries with parsed key/value pairs
# takes a file as argument!
def readFile(fileToRead):
    f = open(fileToRead, "r")
    
    bigDict = {};
    currentName = "";
    currentDict = {}
    for line in f:
        line = line.rstrip("\n")
        if line.endswith("{"):
            currentName = line.split(":")[0]
            continue
        if line == "}":
            bigDict[currentName] = currentDict
            currentDict = {}
            continue
        key = line.split("=")[0].strip();
        value = line.split("=")[1].strip();
        currentDict[key] = value
    
    return bigDict
