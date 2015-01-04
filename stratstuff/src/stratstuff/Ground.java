package stratstuff;

import java.awt.Graphics2D;
import java.util.HashMap;

public class Ground {

	private static HashMap<Integer, GroundInfo> groundMap;
	private static HashMap<String, Integer> byNamesMap;

	public Ground() {

	}

	public static void loadGrounds() {
		groundMap = new HashMap<Integer, GroundInfo>();

		HashMap<String, LoadedInfo> loadedMap = InfoFileReader
				.readFile(FileSystem.DATA_FILE_GROUNDS);

		for (String id : loadedMap.keySet()) {
			int intID = Integer.parseInt(id);
			groundMap.put(intID, new GroundInfo(loadedMap.get(id)));
		}

		createByNamesMap();
	}

	private static void createByNamesMap() {
		byNamesMap = new HashMap<String, Integer>();

		for (int id : groundMap.keySet()) {
			byNamesMap.put(groundMap.get(id).getName(), id);
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

	public static int getByName(String groundName) {
		return byNamesMap.get(groundName);
	}

}
