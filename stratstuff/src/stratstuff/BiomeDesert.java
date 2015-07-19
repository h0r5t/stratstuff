package stratstuff;

public class BiomeDesert extends Biome {

	public BiomeDesert(Area3D bounds) {
		super(bounds);

	}

	@Override
	public World modify(World world) {
		int amountOfCactus = (bounds.getW() * bounds.getH() * bounds.getD())
				/ WorldGeneratorValues.CACTUS_DIVISOR;
		int amountOfBushes = (bounds.getW() * bounds.getH() * bounds.getD())
				/ WorldGeneratorValues.DESERT_BUSH_DIVISOR;

		for (int i = 0; i < amountOfCactus; i++) {
			int cactusID = Element.getByName("cactus");
			WorldPoint wp = generateRandomWP(world, bounds);
			world.setElementForWP(false, wp, cactusID);
		}

		for (int i = 0; i < amountOfBushes; i++) {
			int bushID = Element.getByName("desert_bush");
			WorldPoint wp = generateRandomWP(world, bounds);
			world.setElementForWP(false, wp, bushID);
		}

		return world;
	}

}
