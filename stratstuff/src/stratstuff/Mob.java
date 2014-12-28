package stratstuff;

public abstract class Mob extends MovingObject {

	public Mob(World world) {
		super(world);
	}

	// maybe implement draw here already...

	@Override
	public boolean collides() {
		return false;
	}

}
