package stratstuff;

import java.awt.image.BufferedImage;

public abstract class MovingObject implements Drawable, Saveable {

	private World world;
	protected BufferedImage image;
	private static int uniqueID;
	private static int myType;

	public MovingObject(int id, World world) {
		this.world = world;
		uniqueID = UniqueIDFactory.getID();
		myType = id;
		init();
	}

	public MovingObject(int id, World world, WorldPoint location) {
		this.world = world;
		myType = id;
		uniqueID = UniqueIDFactory.getID();
		init();
		moveTo(location);
	}

	public abstract String getType();

	protected abstract void init();

	public WorldPoint getPosition() {
		return world.getObjectPosition(this);
	}

	public void moveTo(WorldPoint p) {
		world.moveObjectTo(this, p);
	}

	public abstract boolean collides();

	public static MovingObject createFromType(int id, World world) {
		if (id == 0) {
			return new Worker(world);
		}

		return null;
	}

	public int getUniqueID() {
		return uniqueID;
	}

	@Override
	public String save() {
		WorldPoint p = world.getObjectPosition(this);
		return myType + " " + uniqueID + " " + p.getX() + " " + p.getY() + " "
				+ p.getZ();
	}

	@Override
	public Saveable load(String fromString) {
		// TODO Auto-generated method stub
		return null;
	}

	public static int getTypeInt(String string) {
		if (string.equals("worker")) {
			return 0;
		}

		return -1;
	}
}
