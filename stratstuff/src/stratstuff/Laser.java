package stratstuff;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class Laser extends MicroObject {

	private World world;
	private Color laserColor;
	private WorldPoint startWP, targetWP;
	private int vectorX, vectorY;
	private int eventID;
	private int durationMillis;

	public Laser(World world, Color color, int durationMillis,
			WorldPoint startWP, WorldPoint targetWP, int eventID) {
		this.world = world;
		laserColor = color;
		this.startWP = startWP;
		this.vectorX = (targetWP.getX() - startWP.getX())
				* GameSettings.TILE_SIZE;
		this.vectorY = (targetWP.getY() - startWP.getY())
				* GameSettings.TILE_SIZE;
		this.durationMillis = durationMillis;
		this.eventID = eventID;
		this.targetWP = targetWP;
		startWP.addMicroObject(this);
	}

	@Override
	public void update() {
		durationMillis -= GameSettings.TICK_MILLIS;
		if (durationMillis < 0) {
			world.removeMicroObject(this);
			startWP.removeMicroObject(this);
			Core.tellFrontend(FrontendMessaging.eventOccurred(eventID, "true"));
			world.removeElementFromWP(targetWP);
		}
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		Stroke s = g.getStroke();
		g.setStroke(new BasicStroke(2.5f));
		g.setColor(laserColor);
		int xstart = xinpixels + GameSettings.TILE_SIZE / 2;
		int ystart = yinpixels + GameSettings.TILE_SIZE / 2;
		int xend = xstart + vectorX;
		int yend = ystart + vectorY;
		g.drawLine(xstart, ystart, xend, yend);
		g.setStroke(s);

		g.fillOval(xend - 2, yend - 2, 4, 4);
	}

}
