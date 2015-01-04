package stratstuff;

public abstract class Mob extends MovingObject {

	public Mob(int id, World world) {
		super(id, world);
		// TODO Auto-generated constructor stub
	}

	// maybe implement draw here already...

	@Override
	public boolean collides() {
		return false;
	}

}
