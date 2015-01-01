package stratstuff;

import java.io.File;

public class WorldLoader {

	public static World loadWorld(Core main, String worldName) {
		World w = new World(main);

		for (int i = 0; i < GameSettings.WORLD_DEPTH; i++) {
			File f = getLayerFile(i);
		}

		// ....
		return w;
	}

	private static File getLayerFile(int i) {
		return null;
		// return new File();
	}
}
