package stratstuff;

public class WorldGenerator {

	public static World generateWorld(Core main, World onTopOf) {
		// TODO XD
		World world = onTopOf;

		Area3D wholeWorld = new Area3D(0, 0, 0, world.getWidth(),
				world.getHeight(), world.getDepth());

		TerrainGenerator terrainGen = new TerrainGenerator(wholeWorld,
				TerrainType.grass);
		world = terrainGen.modify(world);

		BiomeGenerator biomeGen = new BiomeGenerator();
		world = biomeGen.generateAndModify(world);

		return world;
	}
}
