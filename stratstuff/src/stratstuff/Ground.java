package stratstuff;

import java.awt.Graphics2D;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Ground {

	private static HashMap<Integer, GroundInfo> groundMap;

	public Ground() {

	}

	public static void loadGrounds() {
		try {
			// TODO: load from file
			groundMap = new HashMap<Integer, GroundInfo>();

			groundMap.put(
					0,
					new GroundInfo("grass", ImageIO.read(new File(
							FileSystem.TEXTURES_GROUNDS_DIR + "/0/0.png")),
							false));
			groundMap.put(
					1,
					new GroundInfo("rock", ImageIO.read(new File(
							FileSystem.TEXTURES_GROUNDS_DIR + "/1/0.png")),
							true));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void draw(Graphics2D g, int id, int x, int y) {
		g.drawImage(groundMap.get(id).getImage(), x, y, GameSettings.TILE_SIZE,
				GameSettings.TILE_SIZE, null);
	}

	public static boolean collides(int id) {
		return groundMap.get(id).collides();
	}

	public static String getName(int id) {
		return groundMap.get(id).getName();
	}

}
