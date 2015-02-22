package stratstuff;

public class WorldGenerator {

	public static World generateWorld(Core main) {
		World world = new World(main);

		Area3D wholeWorld = new Area3D(0, 0, 0, GameSettings.WORLD_WIDTH,
				GameSettings.WORLD_HEIGHT, GameSettings.WORLD_DEPTH);

		TerrainGenerator terrainGen = new TerrainGenerator(wholeWorld);
		world = terrainGen.modify(world);

		BiomeGenerator biomeGen = new BiomeGenerator();
		world = biomeGen.generateAndModify(world);

		return world;
	}
}
