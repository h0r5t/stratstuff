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
