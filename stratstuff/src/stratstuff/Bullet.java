package stratstuff;

import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet extends MicroObject {

	private World world;
	private int myType;
	private double microPosX = 50; // position on wp in percent
	private double microPosY = 50;
	private WorldPoint currentWP;
	private double speedX;
	private double speedY;
	private int bulletSpeed = GameSettings.BULLET_SPEED;

	public static final int TYPE_SMALL_BULLET = 0;

	public Bullet(World world, int type, WorldPoint startWP,
			WorldPoint directionWP) {
		this.world = world;
		this.myType = type;
		currentWP = startWP;
		calculateVector(startWP, directionWP);
		currentWP.addMicroObject(this);
	}

	public Bullet(World world, int type, WorldPoint startWP, int degree) {
		this.world = world;
		this.myType = type;
		currentWP = startWP;
		calculateVector(startWP, degree);
		currentWP.addMicroObject(this);
	}

	private void calculateVector(WorldPoint startWP, int degree) {
		int x = (int) (100 * Math.cos(Math.toRadians(degree - 90)) + startWP
				.getX());
		int y = (int) (100 * Math.sin(Math.toRadians(degree - 90)) + startWP
				.getY());
		WorldPoint sampleDirectionWP = new WorldPoint(x, y, 0, 0);
		calculateVector(startWP, sampleDirectionWP);
	}

	private void calculateVector(WorldPoint startWP, WorldPoint directionWP) {
		int distanceX = directionWP.getX() - startWP.getX();
		int distanceY = directionWP.getY() - startWP.getY();
		double distanceDirection = Math.sqrt(distanceX * distanceX + distanceY
				* distanceY);
		speedX = distanceX * (bulletSpeed / distanceDirection);
		speedY = distanceY * (bulletSpeed / distanceDirection);
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		int posX = (int) ((GameSettings.TILE_SIZE * microPosX) / 100);
		int posY = (int) ((GameSettings.TILE_SIZE * microPosY) / 100);

		g.setColor(Color.BLACK);
		g.fillOval(xinpixels + posX, yinpixels + posY, 3, 3);
	}

	@Override
	public void update() {
		microPosX += speedX;
		microPosY += speedY;

		int xmoved = 0;
		int ymoved = 0;
		while (true) {
			if (microPosX > 100) {
				microPosX -= 100;
				xmoved = 1;
			} else if (microPosX < 0) {
				microPosX += 100;
				xmoved = -1;
			}
			if (microPosY > 100) {
				microPosY -= 100;
				ymoved = 1;
			} else if (microPosY < 0) {
				microPosY += 100;
				ymoved = -1;
			}

			if (xmoved != 0 || ymoved != 0) {
				currentWP.removeMicroObject(this);
				int newXPos = currentWP.getX() + xmoved;
				int newYPos = currentWP.getY() + ymoved;
				if (newXPos >= world.getWidth() || newXPos < 0
						|| newYPos >= world.getHeight() || newYPos < 0) {
					// out of world bounds
					currentWP = null;
					world.removeMicroObject(this);
					return;
				}
				WorldPoint newWP = world.getWP(newXPos, newYPos,
						currentWP.getZ());
				if (newWP.collides()) {
					currentWP.removeMicroObject(this);
					world.removeMicroObject(this);
					return;
				}
				newWP.addMicroObject(this);
				currentWP = newWP;
				xmoved = 0;
				ymoved = 0;
			} else {
				break;
			}
		}

	}
}
