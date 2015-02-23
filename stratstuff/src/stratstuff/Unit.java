package stratstuff;

import java.util.ArrayList;
import java.util.HashMap;

public class Unit implements Saveable {

	private int myType;
	private int myUniqueID;
	private MovingObject myObject;
	private ArrayList<Item> inventory;
	private static HashMap<String, LoadedInfo> info;

	public Unit(World w, int uniqueID, int unitType, int objectUID) {
		LoadedInfo myInfo = info.get(unitType + "");
		myUniqueID = uniqueID;
		myType = unitType;
		myObject = w.getObjectByUID(objectUID);
		inventory = new ArrayList<Item>();
	}

	public int getUniqueID() {
		return myUniqueID;
	}

	public void addToInventory(Item item) {
		inventory.add(item);
	}

	@Override
	public String save() {
		return myUniqueID + " " + myType + " " + myObject.getUniqueID();
	}

	@Override
	public Saveable load(String fromString) {
		return null;
	}

	public static void loadFromInfoFile() {
		info = InfoFileReader.readFile(FileSystem.DATA_FILE_UNITS);
	}

}
