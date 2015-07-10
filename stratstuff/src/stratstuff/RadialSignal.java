package stratstuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class RadialSignal extends Signal {

	private World world;
	private WorldPoint source;
	private Color color = Color.RED;
	private int currentRadius = 0;
	private int radiusIncrease = 5;
	private final int maxRadius = 255;
	private MovingObject senderObj;

	public RadialSignal(World world, WorldPoint source, MovingObject senderObj,
			String message) {
		super(source, message);
		this.world = world;
		this.source = source;
		this.message = message;
		this.senderObj = senderObj;
		source.addMicroObject(this);
	}

	private Color lowerAlpha(Color c) {
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()
				- radiusIncrease);
	}

	@Override
	public void update() {
		super.update();
		currentRadius += radiusIncrease;
		if (currentRadius > maxRadius) {
			source.removeMicroObject(this);
			world.removeMicroObject(this);
			checkForReceivers();
			return;
		}
		color = lowerAlpha(color);
	}

	@Override
	protected void checkForReceivers() {

		int tileRadius = (maxRadius / GameSettings.TILE_SIZE) / 2;
		for (int i = -tileRadius; i <= tileRadius; i++) {
			for (int o = -tileRadius; o <= tileRadius; o++) {
				int x = source.getX() + i;
				int y = source.getY() + o;
				int z = source.getZ();
				ArrayList<MovingObject> list = world.getWP(x, y, z)
						.getAttachedMovingObjects();

				for (MovingObject obj : list) {
					if (obj.getUniqueID() != senderObj.getUniqueID()) {
						Core.tellFrontend(FrontendMessaging.signalReceived(
								obj.getUniqueID(), this));
					}
				}
			}
		}
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		g.setColor(color);
		g.drawOval(xinpixels - currentRadius + GameSettings.TILE_SIZE / 2,
				yinpixels - currentRadius + GameSettings.TILE_SIZE / 2,
				currentRadius * 2, currentRadius * 2);
	}

}
