package stratstuff;

public class TerrainGenerator extends WorldPartModificator {

	private TerrainType surfaceType;

	public TerrainGenerator(Area3D bounds, TerrainType type) {
		super(bounds);
		this.surfaceType = type;
	}

	@Override
	public World modify(World world) {
		setSurface(world);
		setUnderground(world);
		createMinerals(world);
		return world;
	}

	private void createMinerals(World world) {
		int amountOfCoal = (bounds.getW() * bounds.getH() * bounds.getD())
				/ WorldGeneratorValues.COAL_DIVISOR;
		int amountOfIron = (bounds.getW() * bounds.getH() * bounds.getD())
				/ WorldGeneratorValues.IRON_DIVISOR;
		int amountOfDiamond = (bounds.getW() * bounds.getH() * bounds.getD())
				/ WorldGeneratorValues.DIAMOND_DIVISOR;

		Area3D underGroundBounds = new Area3D(0, 0, 1, bounds.getW(),
				bounds.getH(), world.getDepth());

		for (int i = 0; i < amountOfCoal; i++) {
			int id = Element.getByName("coal");
			WorldPoint wp = generateRandomWP(world, underGroundBounds);
			world.setElementForWP(false, wp, id);
		}

		for (int i = 0; i < amountOfIron; i++) {
			int id = Element.getByName("iron");
			WorldPoint wp = generateRandomWP(world, underGroundBounds);
			world.setElementForWP(false, wp, id);
		}

		for (int i = 0; i < amountOfDiamond; i++) {
			int id = Element.getByName("diamond");
			WorldPoint wp = generateRandomWP(world, underGroundBounds);
			world.setElementForWP(false, wp, id);
		}
	}

	private void setSurface(World world) {
		int groundType = Ground.getByName(surfaceType.toString());
		int surfaceLayer = 0;
		for (int y = 0; y < bounds.getH(); y++) {
			for (int x = 0; x < bounds.getW(); x++) {
				world.addWorldPoint(new WorldPoint(x, y, surfaceLayer,
						groundType));
			}
		}
	}

	private void setUnderground(World world) {
		for (int z = 1; z < bounds.getD(); z++) {
			for (int y = 0; y < bounds.getH(); y++) {
				for (int x = 0; x < bounds.getW(); x++) {
					WorldPoint p = new WorldPoint(x, y, z,
							Ground.getByName("rock"));
					world.addWorldPoint(p);
					world.setElementForWP(false, p,
							Element.getByName("raw_stone"));
				}
			}
		}

	}
}
