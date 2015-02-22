package stratstuff;

public abstract class WorldPartModificator {

	protected Area3D bounds;

	public WorldPartModificator(Area3D bounds) {
		this.bounds = bounds;
	}

	public abstract World modify(World world);

	protected WorldPoint generateRandomWP(World world, Area3D bounds) {
		int randx = (int) (Math.random() * bounds.getW() - 1) + bounds.getX();
		int randy = (int) (Math.random() * bounds.getH() - 1) + bounds.getY();
		int randz = (int) (Math.random() * bounds.getD() - 1) + bounds.getZ();

		return world.getWP(randx, randy, randz);
	}
}
