package stratstuff;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

public class Unit implements Saveable {

	private int myType;
	private int myUniqueID;
	private MovingObject myObject;
	private ArrayList<Item> inventory;
	private Design design;
	private static HashMap<String, LoadedInfo> info;

	public Unit(Core core, World w, int uniqueID, int unitType, int objectUID,
			String designName) {
		LoadedInfo myInfo = info.get(unitType + "");
		myUniqueID = uniqueID;
		myType = unitType;
		myObject = w.getObjectByUID(objectUID);
		inventory = new ArrayList<Item>();
		if (!designName.equals("null")) {
			design = core.getDeveloperFrame().getDesignByName(designName);
		}
	}

	public Design getDesign() {
		return design;
	}

	@SuppressWarnings("deprecation")
	// ugly, only used once after load
	public void registerRobot() {
		if (design == null)
			return;
		Core.tellFrontend(FrontendMessaging.startRobotWithDesign(
				myObject.getUniqueID(), design.getName()));
	}

	public void setDesign(Design d) {
		design = d;
	}

	public void removeDesign() {
		design = null;
	}

	public int getUniqueID() {
		return myUniqueID;
	}

	public void addToInventory(Item item) {
		inventory.add(item);
	}

	public int getMovingObjUID() {
		return myObject.getUniqueID();
	}

	public void fireBullet(Core core) {
		Bullet b = new Bullet(core, 0, myObject.getPosition(),
				myObject.getCurrentAngleInDegrees());
		core.getWorld().addMicroObject(b);
	}

	public void fireBullet(Core core, WorldPoint target) {
		Bullet b = new Bullet(core, 0, myObject.getPosition(), target);
		core.getWorld().addMicroObject(b);
	}

	public void mineWorldPoint(Core core, int eventID, WorldPoint wp) {
		Color color = new Color(0x33, 0xCC, 0xFF);
		Laser laser = new Laser(core, color, 5000, myObject.getPosition(), wp,
				eventID);
		core.getWorld().addMicroObject(laser);
	}

	public VisionScope getVisionScope(Core core) {
		return new VisionScope(core, this);
	}

	@Override
	public String save() {
		String designName = "null";
		if (design != null) {
			designName = design.getName();
		}
		return myUniqueID + " " + myType + " " + myObject.getUniqueID() + " "
				+ designName;
	}

	@Override
	public Saveable load(String fromString) {
		return null;
	}

	public static void loadFromInfoFile() {
		info = InfoFileReader.readFile(FileSystem.DATA_FILE_UNITS);
	}
}
