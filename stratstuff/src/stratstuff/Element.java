package stratstuff;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Element {

	private static HashMap<Integer, ElementInfo> elementMap;
	private static HashMap<String, Integer> namesMap;

	public static void loadElements() {
		try {
			// TODO: load from file
			elementMap = new HashMap<Integer, ElementInfo>();
			namesMap = new HashMap<String, Integer>();

			elementMap.put(
					0,
					new ElementInfo("tree", ImageIO.read(new File(
							FileSystem.TEXTURES_ELEMENTS_DIR + "/0/0.png")),
							true, false, false));
			elementMap.put(
					1,
					new ElementInfo("bush", ImageIO.read(new File(
							FileSystem.TEXTURES_ELEMENTS_DIR + "/1/0.png")),
							false, false, false));

			elementMap.put(
					2,
					new ElementInfo("ladderdown", ImageIO.read(new File(
							FileSystem.TEXTURES_ELEMENTS_DIR + "/2/0.png")),
							false, true, false));

			elementMap.put(
					3,
					new ElementInfo("ladderup", ImageIO.read(new File(
							FileSystem.TEXTURES_ELEMENTS_DIR + "/3/0.png")),
							false, false, true));

			createNamesMap();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void createNamesMap() {
		for (int i : elementMap.keySet()) {
			ElementInfo info = elementMap.get(i);
			namesMap.put(info.getName(), i);
		}
	}

	public static void draw(Graphics2D g, int id, int x, int y) {
		g.drawImage(elementMap.get(id).getImage(), x, y,
				GameSettings.TILE_SIZE, GameSettings.TILE_SIZE, null);
	}

	public static int getByName(String name) {
		return namesMap.get(name);
	}

	public static boolean collides(int id) {
		return elementMap.get(id).collides();
	}

	public static String getName(int id) {
		return elementMap.get(id).getName();
	}

	public static boolean isLadderDown(int id) {
		return elementMap.get(id).isLadderDown();
	}

	public static boolean isLadderUp(int id) {
		return elementMap.get(id).isLadderUp();
	}

}
