package stratstuff;

import java.awt.image.BufferedImage;

public abstract class MovingObject implements Drawable {

	private World world;
	protected BufferedImage image;

	public MovingObject(World world) {
		this.world = world;
		init();
	}

	public abstract String getType();

	protected abstract void init();

	public WorldPoint getPosition() {
		return world.getObjectPosition(this);
	}

	public void moveTo(WorldPoint p) {
		world.moveObjectTo(this, p);
	}

	public abstract boolean collides();

}
