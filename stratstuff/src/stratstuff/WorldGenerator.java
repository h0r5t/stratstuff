package stratstuff;

public class WorldGenerator {

	public static World generateWorld(Core main) {
		World w = new World(main);

		for (int z = 0; z < GameSettings.WORLD_DEPTH; z++) {
			for (int y = 0; y < GameSettings.WORLD_HEIGHT; y++) {
				for (int x = 0; x < GameSettings.WORLD_WIDTH; x++) {
					w.addWorldPoint(new WorldPoint(x, y, z, Ground
							.getByName("grass")));
				}
			}
		}

		int amount = 1000;
		for (int a = 0; a < amount; a++) {
			int x = (int) (Math.random() * GameSettings.WORLD_WIDTH - 1);
			int y = (int) (Math.random() * GameSettings.WORLD_HEIGHT - 1);
			int z = 0;

			w.setElementForWP(true, w.getWP(x, y, z), Element.getByName("tree"));
		}

		amount = 500;
		for (int a = 0; a < amount; a++) {
			int x = (int) (Math.random() * GameSettings.WORLD_WIDTH - 1);
			int y = (int) (Math.random() * GameSettings.WORLD_HEIGHT - 1);
			int z = 0;

			w.setElementForWP(true, w.getWP(x, y, z), Element.getByName("bush"));
		}

		return w;
	}
}
