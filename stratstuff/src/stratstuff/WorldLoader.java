package stratstuff;

public class WorldLoader {

	public WorldLoader() {
	}

	public static World loadWorld(Main main, String worldName) {
		World w = new World(main);

		// todo THIS IS TEST STUFF RIGHT NOW

		for (int z = 0; z < GameSettings.WORLD_DEPTH; z++) {
			for (int i = 0; i < GameSettings.WORLD_WIDTH; i++) {
				for (int o = 0; o < GameSettings.WORLD_HEIGHT; o++) {
					w.addWorldPoint(new WorldPoint(i, o, z, 0));
				}
			}

		}

		int amount = 3000;
		for (int a = 0; a < amount; a++) {
			int x = (int) (Math.random() * GameSettings.WORLD_WIDTH);
			int y = (int) (Math.random() * GameSettings.WORLD_HEIGHT);
			w.attachElement(true, w.getWP(x, y, 0), 0);
		}
		amount = 1000;
		for (int a = 0; a < amount; a++) {
			int x = (int) (Math.random() * GameSettings.WORLD_WIDTH);
			int y = (int) (Math.random() * GameSettings.WORLD_HEIGHT);
			w.attachElement(true, w.getWP(x, y, 0), 1);
		}
		return w;
	}
}
