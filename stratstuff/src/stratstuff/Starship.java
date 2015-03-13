package stratstuff;

import java.awt.Graphics2D;

public class Starship extends FloatingObject {

	private int diameter;
	private DynamicTexture texture;

	public Starship(int ID, String name, SpacePosition pos, int diameter) {
		super(ID, name, pos);
		this.diameter = diameter;
		this.texture = new DynamicTexture(
				TextureGenerator.generateStarshipImage(5, 10), 10, 0);
	}

	public DynamicTexture getDynamicTexture() {
		return texture;
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		g.drawImage(texture.getImage(), xinpixels, yinpixels, null);
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
