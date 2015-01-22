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
	private boolean paint = true;
	private static HashMap<String, LoadedInfo> info;

	public MovingObject(int myType, World world) {
		LoadedInfo myInfo = info.get(myType + "");
		name = myInfo.getValueString("name");
		try {
			image = ImageIO.read(new File(FileSystem.TEXTURES_DIR + "/units/"
					+ myInfo.getValueString("image")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.collides = myInfo.getValueBool("collides");
		this.world = world;
		uniqueID = UniqueIDFactory.getID();
		this.myType = myType;
	}

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
		this.world = world;
		this.uniqueID = uniqueID;
		this.myType = myType;
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

	public void setPaintBool(boolean bool) {
		paint = bool;
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		if (paint) {
			g.drawImage(image, xinpixels, yinpixels, GameSettings.TILE_SIZE,
					GameSettings.TILE_SIZE, null);
		}
	}

	// ------------ static -----------

	public static void loadFromInfoFile() {
		info = InfoFileReader.readFile(FileSystem.DATA_FILE_OBJECTS);
	}
}
