package stratstuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Sector implements Drawable {

	public static final int sectorWidth = GameSettings.GAME_FRAME_WIDTH;
	public static final int sectorHeight = GameSettings.GAME_FRAME_HEIGHT;

	private ArrayList<FloatingObject> floatingObjects;
	private Galaxy galaxy;

	private int xpos;
	private int ypos;

	public Sector(Galaxy galaxy, int xpos, int ypos) {
		this.galaxy = galaxy;
		floatingObjects = new ArrayList<FloatingObject>();
		this.xpos = xpos;
		this.ypos = ypos;
	}

	public int getXPos() {
		return xpos;
	}

	public int getYPos() {
		return ypos;
	}

	public void addObject(FloatingObject o) {
		floatingObjects.add(o);
	}

	public Starship getRandomStarship() {
		for (FloatingObject o : floatingObjects) {
			if (o instanceof Starship)
				return (Starship) o;
		}

		return null;
	}

	public void removeObject(FloatingObject o) {
		floatingObjects.remove(o);
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		g.setColor(Color.BLACK);
		g.fillRect(xinpixels, yinpixels, sectorWidth, sectorHeight);

		for (FloatingObject object : floatingObjects) {
			object.draw(g, xinpixels + (int) object.getPosition().getMicX(),
					yinpixels + (int) object.getPosition().getMicY());
		}
	}

	public ArrayList<FloatingObject> getFloatingObjects() {
		return floatingObjects;
	}

}