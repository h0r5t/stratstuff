package stratstuff;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Sector implements Drawable, Updatable {

	public static final int sectorWidth = GameSettings.GAME_FRAME_WIDTH;
	public static final int sectorHeight = GameSettings.GAME_FRAME_HEIGHT;

	private ArrayList<FloatingObject> floatingObjects;
	private Galaxy galaxy;
	private BufferedImage backgroundImage;
	private int xpos;
	private int ypos;

	public Sector(Galaxy galaxy, int xpos, int ypos) {
		this.galaxy = galaxy;
		floatingObjects = new ArrayList<FloatingObject>();
		this.xpos = xpos;
		this.ypos = ypos;
		backgroundImage = TextureGenerator.generateSectorBackground();
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

	public ArrayList<Starship> getAllStarships() {
		ArrayList<Starship> shipList = new ArrayList<Starship>();
		for (FloatingObject obj : floatingObjects) {
			if (obj instanceof Starship) {
				shipList.add((Starship) obj);
			}
		}

		return shipList;
	}

	public void removeObject(FloatingObject o) {
		floatingObjects.remove(o);
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		g.drawImage(backgroundImage, xinpixels, yinpixels, sectorWidth,
				sectorHeight, null);

		for (FloatingObject object : floatingObjects) {
			object.draw(g, xinpixels + (int) object.getPosition().getMicX(),
					yinpixels + (int) object.getPosition().getMicY());
		}
	}

	public ArrayList<FloatingObject> getFloatingObjects() {
		return floatingObjects;
	}

	@Override
	public void update() {
		for (FloatingObject obj : floatingObjects) {
			obj.update();
		}
	}
}
