package stratstuff;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Planet extends FloatingObject {

	private int diameter;
	private BufferedImage image;

	public Planet(int ID, String name, SpacePosition pos, BufferedImage image) {
		super(ID, name, pos);
		this.image = image;
		this.diameter = image.getHeight();
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		g.drawImage(image, xinpixels, yinpixels, diameter, diameter, null);
	}

	@Override
	public String save() {
		return FloatingObject.ID_PLANET + " " + ID + " " + position.getMicX()
				+ " " + position.getMicY() + " " + name;
	}

	@Override
	public Saveable load(String fromString) {
		return null;
	}

}
