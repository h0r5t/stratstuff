package stratstuff;

public abstract class Biome extends WorldPartModificator {

	public Biome(Area3D bounds) {
		super(bounds);
	}

	@Override
	public abstract World modify(World world);

}
