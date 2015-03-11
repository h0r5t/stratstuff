package stratstuff;

import java.awt.Color;
import java.awt.Graphics2D;

public class Starship extends FloatingObject {

	private int diameter;

	public Starship(int ID, String name, SpacePosition pos, int diameter) {
		super(ID, name, pos);
		this.diameter = diameter;
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		g.setColor(Color.GRAY);
		g.fillOval(xinpixels, yinpixels, diameter, diameter);
	}

	@Override
	public String save() {
		return FloatingObject.ID_STARSHIP + " " + ID + " " + position.getMicX()
				+ " " + position.getMicY() + " " + name + " " + diameter;
	}

	@Override
	public Saveable load(String fromString) {
		return null;
	}

}
