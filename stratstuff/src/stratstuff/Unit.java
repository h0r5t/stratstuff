package stratstuff;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

public class Unit implements Saveable {

	private int myType;
	private int myUniqueID;
	private MovingObject myObject;
	private ArrayList<Item> inventory;
	private RobotDesign design;
	private static HashMap<String, LoadedInfo> info;
	private World world;
	private ArrayList<ContextCommand> contextCommands;

	public Unit(Core core, World w, int uniqueID, int unitType, int objectUID, String designName) {
		LoadedInfo myInfo = info.get(unitType + "");
		this.world = w;
		myUniqueID = uniqueID;
		myType = unitType;
		myObject = w.getObjectByUID(objectUID);
		inventory = new ArrayList<Item>();
		contextCommands = new ArrayList<ContextCommand>();
		if (!designName.equals("null")) {
			design = core.getDeveloperFrame().getDesignByName(designName);
			contextCommands = ContextCommand.scanDesignFile(this, design);
		}
	}

	public RobotDesign getDesign() {
		return design;
	}

	public ArrayList<ContextCommand> getContextCommands() {
		return contextCommands;
	}

	@SuppressWarnings("deprecation")
	// ugly, only used once after load
	public void registerRobot() {
		if (design == null)
			return;
		Core.tellFrontend(FrontendMessaging.startRobotWithDesign(myObject.getUniqueID(), design.getName()));
	}

	public void setDesign(RobotDesign d) {
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
		Core.tellFrontend(FrontendMessaging.addToInventory(getMovingObjUID(), item));
	}

	public void removeItemFromInventory(Item item) {
		inventory.remove(item);
		Core.tellFrontend(FrontendMessaging.removeFromInventory(getMovingObjUID(), item));
	}

	public void initItemsInFrontend() {
		for (Item item : inventory) {
			Core.tellFrontend(FrontendMessaging.addToInventory(getMovingObjUID(), item));
		}
	}

	public int getMovingObjUID() {
		return myObject.getUniqueID();
	}

	public MovingObject getMovingObject() {
		return myObject;
	}

	public void fireBullet(World world) {
		Bullet b = new Bullet(world, 0, myObject.getPosition(), myObject.getCurrentAngleInDegrees());
		world.addMicroObject(b);
	}

	public void fireBullet(World world, WorldPoint target) {
		Bullet b = new Bullet(world, 0, myObject.getPosition(), target);
		world.addMicroObject(b);
	}

	public void mineWorldPoint(World world, int eventID, WorldPoint wp) {
		Color color = new Color(0x33, 0xCC, 0xFF);
		Laser laser = new Laser(world, color, 5000, myObject.getPosition(), wp, eventID);
		world.addMicroObject(laser);
	}

	public VisionScope getVisionScope(Core core) {
		return new VisionScope(this);
	}

	@Override
	public String save() {
		String designName = "null";
		if (design != null) {
			designName = design.getName();
		}
		return myUniqueID + " " + myType + " " + myObject.getUniqueID() + " " + designName;
	}

	@Override
	public Saveable load(String fromString) {
		return null;
	}

	public static void loadFromInfoFile() {
		info = InfoFileReader.readFile(FileSystem.DATA_FILE_UNITS);
	}

	public World getWorld() {
		return world;
	}

}
