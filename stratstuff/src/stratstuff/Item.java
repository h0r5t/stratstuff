package stratstuff;

import java.util.HashMap;

public class Item implements Saveable {

	private int uniqueID;
	private int itemType;
	private MovingObject linkedObject;
	private Unit ownerUnit;
	private String infoText;
	private static HashMap<String, LoadedInfo> info;
	private static LoadedInfo myInfo;
	private World world;

	public Item(World w, int uniqueID, int itemType, int linkedObjUID,
			int ownerUnitUID, String infoText) {
		this.world = w;
		myInfo = info.get(itemType + "");
		this.uniqueID = uniqueID;
		this.itemType = itemType;
		linkedObject = w.getObjectByUID(linkedObjUID);
		ownerUnit = w.getUnitByID(ownerUnitUID);
		if (ownerUnit != null)
			ownerUnit.addToInventory(this);
		this.infoText = infoText;
	}

	public int getUniqueID() {
		return uniqueID;
	}

	public int getLinkedObjUniqueID() {
		if (linkedObject == null)
			return -1;
		return linkedObject.getUniqueID();
	}

	public int getType() {
		return itemType;
	}

	@Override
	public String save() {
		String linkedObjString = "-1";
		if (linkedObject != null) {
			linkedObjString = "" + linkedObject.getUniqueID();
		}
		String ownerUnitIDString = "-1";
		if (ownerUnit != null) {
			ownerUnitIDString = "" + ownerUnit.getUniqueID();
		}
		return uniqueID + " " + itemType + " " + linkedObjString + " "
				+ ownerUnitIDString + " " + infoText;
	}

	@Override
	public Saveable load(String fromString) {
		return null;
	}

	public void pickedUpBy(Unit unit) {
		ownerUnit = unit;
		ownerUnit.addToInventory(this);

		linkedObject.getWorld().removeObjectFromWorld(linkedObject);
		linkedObject = null;
	}

	// places item, will be turned to element
	public void placeItem(Unit u) {
		WorldPoint placeLocation = u.getMovingObject().getPosition();
		if (placeLocation.getAttachedElement() != -1) {
			// element already present, cant place item here
			return;
		}
		int elementID = myInfo.getValueInt("element");
		u.getWorld().setElementForWP(false, placeLocation, elementID);

		u.removeItemFromInventory(this);
		world.removeItem(this);
	}

	// drops item, which just removes from Inventory and creates movingobject
	public void dropItem(Unit u) {
		WorldPoint dropLocation = u.getMovingObject().getPosition();
		int droppedObjType = getLinkedObjectType();
		MovingObject newObject = new MovingObject(droppedObjType, world,
				UniqueIDFactory.getID());
		world.spawnObject(newObject, dropLocation);

		ownerUnit.removeItemFromInventory(this);
		ownerUnit = null;
		linkedObject = newObject;
	}

	public static void loadFromInfoFile() {
		info = InfoFileReader.readFile(FileSystem.DATA_FILE_ITEMS);
	}

	public int getLinkedObjectType() {
		return myInfo.getValueInt("object");
	}

	public static int getLinkedObjectType(int itemType) {
		return info.get("" + itemType).getValueInt("object");
	}

	public World getWorld() {
		return world;
	}
}
