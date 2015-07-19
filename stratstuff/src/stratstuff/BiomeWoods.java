package stratstuff;

public class BiomeWoods extends Biome {

	public BiomeWoods(Area3D bounds) {
		super(bounds);
	}

	@Override
	public World modify(World world) {
		int amountOfTrees = (bounds.getW() * bounds.getH() * bounds.getD())
				/ WorldGeneratorValues.TREES_DIVISOR;
		int amountOfBushes = (bounds.getW() * bounds.getH() * bounds.getD())
				/ WorldGeneratorValues.BUSH_DIVISOR;

		for (int i = 0; i < amountOfTrees; i++) {
			int treeID = Element.getByName("tree");
			WorldPoint wp = generateRandomWP(world, bounds);
			world.setElementForWP(false, wp, treeID);
		}

		for (int i = 0; i < amountOfBushes; i++) {
			int bushID = Element.getByName("bush");
			WorldPoint wp = generateRandomWP(world, bounds);
			world.setElementForWP(false, wp, bushID);
		}

		return world;
	}
}
