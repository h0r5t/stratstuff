package stratstuff;

import java.awt.Graphics2D;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Element {

	private static HashMap<Integer, ElementInfo> elementMap;

	public static void loadElements() {
		try {
			// TODO: load from file
			elementMap = new HashMap<Integer, ElementInfo>();

			elementMap.put(
					0,
					new ElementInfo("tree", ImageIO.read(new File(
							FileSystem.TEXTURES_ELEMENTS_DIR + "/0/0.png")),
							true));
			elementMap.put(
					1,
					new ElementInfo("bush", ImageIO.read(new File(
							FileSystem.TEXTURES_ELEMENTS_DIR + "/1/0.png")),
							false));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void draw(Graphics2D g, int id, int x, int y) {
		g.drawImage(elementMap.get(id).getImage(), x, y,
				GameSettings.TILE_SIZE, GameSettings.TILE_SIZE, null);
	}

	public static boolean collides(int id) {
		return elementMap.get(id).collides();
	}

	public static String getName(int id) {
		return elementMap.get(id).getName();
	}

}
