package stratstuff;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class MovingObject implements Drawable, Saveable {

	private World world;
	private BufferedImage image;
	private int uniqueID;
	private int myType;
	private String name;
	private boolean collides;
	private boolean canPickUp;
	private boolean paint = true;
	private DynamicTexture texture;
	private static HashMap<String, LoadedInfo> info;

	public MovingObject(int myType, World world, int uniqueID) {
		LoadedInfo myInfo = info.get(myType + "");
		name = myInfo.getValueString("name");
		try {
			image = ImageIO.read(new File(FileSystem.TEXTURES_DIR + "/units/"
					+ myInfo.getValueString("image")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.collides = myInfo.getValueBool("collides");
		this.canPickUp = myInfo.getValueBool("canpickup");
		this.world = world;
		this.uniqueID = uniqueID;
		this.myType = myType;
		this.texture = new DynamicTexture(image, 10, 2);
	}

	public void updateRotation(WorldPoint nextWP) {
		texture.updateRotationDirection(getPosition(), nextWP);
	}

	public void turnToFaceWorldPoint(int eventID, WorldPoint wpToFace) {
		texture.turnDirectionVector(getPosition(), wpToFace, eventID);
	}

	public void update() {
		texture.update();
	}

	public WorldPoint getPosition() {
		return world.getObjectPosition(this);
	}

	public void moveTo(WorldPoint p) {
		world.moveObjectTo(this, p);
	}

	public boolean collides() {
		return collides;
	}

	public boolean canPickUp() {
		return canPickUp;
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
		return null;
	}

	public int getTypeInt() {
		return myType;
	}

	public String getName() {
		return name;
	}

	public World getWorld() {
		return world;
	}

	public int getCurrentAngleInDegrees() {
		return texture.getCurrentAngle();
	}

	public void setPaintBool(boolean bool) {
		paint = bool;
	}

	public boolean hasTurned() {
		return texture.hasTurned();
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		if (paint) {
			g.drawImage(texture.getImage(), xinpixels, yinpixels,
					GameSettings.TILE_SIZE, GameSettings.TILE_SIZE, null);
		}
	}

	// ------------ static -----------

	public static void loadFromInfoFile() {
		info = InfoFileReader.readFile(FileSystem.DATA_FILE_OBJECTS);
	}
}
