package stratstuff;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class LightManager implements Updatable {

	// light levels 0-5, 0 = darkest

	private World world;
	private int[][][] lightArray;
	private ArrayList<WorldPoint> lightSources;
	private HashMap<Integer, Image> imageMap;

	public LightManager(World world) {
		this.world = world;
		lightArray = new int[world.getWidth()][world.getHeight()][world
				.getDepth()];
		lightSources = new ArrayList<WorldPoint>();
		try {
			initImages();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initImages() throws IOException {
		String texDir = FileSystem.TEXTURES_DIR + "/shadows/";
		imageMap = new HashMap<Integer, Image>();
		imageMap.put(0, ImageIO.read(new File(texDir + "0.png")));
		imageMap.put(1, ImageIO.read(new File(texDir + "1.png")));
		imageMap.put(2, ImageIO.read(new File(texDir + "2.png")));
		imageMap.put(3, ImageIO.read(new File(texDir + "3.png")));
		imageMap.put(4, ImageIO.read(new File(texDir + "4.png")));
		imageMap.put(5, ImageIO.read(new File(texDir + "5.png")));
		imageMap.put(6, ImageIO.read(new File(texDir + "6.png")));
		imageMap.put(7, ImageIO.read(new File(texDir + "7.png")));
		imageMap.put(8, ImageIO.read(new File(texDir + "8.png")));
		imageMap.put(9, ImageIO.read(new File(texDir + "9.png")));
		imageMap.put(10, ImageIO.read(new File(texDir + "10.png")));
	}

	public void initLights() {
		for (int x = 0; x < world.getWidth(); x++) {
			for (int y = 0; y < world.getHeight(); y++) {
				for (int z = 0; z < world.getDepth(); z++) {
					if (z > 0) {
						lightArray[x][y][z] = GameSettings.LIGHT_LEVEL_UNDERGROUND;
					} else {
						lightArray[x][y][z] = GameSettings.LIGHT_LEVEL_SURFACE;
					}
				}
			}
		}

		for (int z = 0; z < world.getDepth(); z++) {
			for (int x = 0; x < world.getWidth(); x++) {
				for (int y = 0; y < world.getHeight(); y++) {
					int elementID = world.getWP(x, y, z).getAttachedElement();
					if (Element.isLightSource(elementID)) {
						registerLightSource(x, y, z);
					}
				}
			}
		}
		updateLight();
	}

	public void registerLightSource(int x, int y, int z) {
		WorldPoint p = world.getWP(x, y, z);
		lightSources.add(p);
		updateLightLevel(p);
	}

	public void registerLightSource(WorldPoint point) {
		lightSources.add(point);
		updateLightLevel(point);
	}

	private void updateLightLevel(WorldPoint p) {
		if (!lightSources.contains(p)) {
			return;
		}

		int x = p.getX();
		int y = p.getY();
		int z = p.getZ();

		for (int yy = y - 10; yy <= y + 10; yy++) {
			for (int xx = x - 10; xx <= x + 10; xx++) {
				if (xx >= 0 && xx < world.getWidth() && yy >= 0
						&& yy < world.getHeight()) {
					makeBright(xx - x, yy - y, xx, yy, z);
				}
			}
		}
	}

	private void makeBright(int xdistance, int ydistance, int x, int y, int z) {
		double distance = Math.sqrt((xdistance * xdistance)
				+ (ydistance * ydistance));
		int newLevel = distanceToLightLevel(distance);
		int currentLevel = lightArray[x][y][z];
		int testlevel = currentLevel + newLevel;
		if (testlevel > 10) {
			testlevel = 10;
		}
		lightArray[x][y][z] = testlevel;
		// seems to work?
	}

	private int distanceToLightLevel(double distance) {
		distance = Math.round(distance);
		if (distance > 10)
			return 0;
		if (distance > 9)
			return 1;
		if (distance > 8)
			return 2;
		if (distance > 7)
			return 3;
		if (distance > 6)
			return 4;
		if (distance > 5)
			return 5;
		if (distance > 4)
			return 6;
		if (distance > 3)
			return 7;
		if (distance > 2)
			return 8;
		if (distance > 1)
			return 9;
		return 10;
	}

	public void unregisterLightSource(int x, int y, int z) {
		WorldPoint p = world.getWP(x, y, z);
		lightSources.remove(p);
		updateLight();
	}

	private void updateLight() {
		for (int z = 0; z < world.getDepth(); z++) {
			for (int x = 0; x < world.getWidth(); x++) {
				for (int y = 0; y < world.getHeight(); y++) {
					if (z > 0) {
						lightArray[x][y][z] = GameSettings.LIGHT_LEVEL_UNDERGROUND;
					} else {
						lightArray[x][y][z] = GameSettings.LIGHT_LEVEL_SURFACE;
					}
				}
			}
		}
		for (WorldPoint p : lightSources) {
			updateLightLevel(p);
		}
	}

	public boolean isDarkAt(WorldPoint p) {
		if (p == null)
			return false;
		if (p.getX() > 0 && p.getX() < world.getWidth() && p.getY() > 0
				&& p.getY() < world.getHeight() && p.getZ() > 0
				&& p.getZ() < world.getDepth()) {

			if (lightArray[p.getX()][p.getY()][p.getZ()] == 0) {
				return true;
			}
		}

		return false;
	}

	public Image getShadowImage(WorldPoint p) {
		return imageMap.get(lightArray[p.getX()][p.getY()][p.getZ()]);
	}

	@Override
	public void update() {
	}

	public void unregisterLightSource(WorldPoint old) {
		lightSources.remove(old);
		updateLight();
	}

}
