package stratstuff;

public class WorldGenerator {

	public static World generateWorld(Core main, World onTopOf) {
		// TODO XD
		int width = (int) (Math.random() * 150);
		int height = (int) (Math.random() * 150);
		int depth = (int) (Math.random() * 10);

		World world = onTopOf;

		Area3D wholeWorld = new Area3D(0, 0, 0, width, height, depth);

		TerrainGenerator terrainGen = new TerrainGenerator(wholeWorld);
		world = terrainGen.modify(world);

		BiomeGenerator biomeGen = new BiomeGenerator();
		world = biomeGen.generateAndModify(world);

		return world;
	}
}
