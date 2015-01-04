package stratstuff;

import java.awt.Graphics2D;
import java.util.HashMap;

public class Element {

	private static HashMap<Integer, ElementInfo> elementMap;
	private static HashMap<String, Integer> namesMap;

	public static void loadElements() {
		elementMap = new HashMap<Integer, ElementInfo>();
		namesMap = new HashMap<String, Integer>();

		HashMap<String, LoadedInfo> loadedMap = InfoFileReader
				.readFile(FileSystem.DATA_FILE_ELEMENTS);

		for (String id : loadedMap.keySet()) {
			int intID = Integer.parseInt(id);
			elementMap.put(intID, new ElementInfo(loadedMap.get(id)));
		}

		createNamesMap();
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
