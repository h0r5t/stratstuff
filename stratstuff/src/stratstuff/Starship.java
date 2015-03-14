package stratstuff;

import java.awt.Graphics2D;

public class Starship extends FloatingObject implements UISelectable {

	private int diameter;
	private DynamicTexture texture;
	private boolean uiSelected = false;
	private SelectionIndicator selectionIndicator;

	public Starship(int ID, String name, SpacePosition pos, int diameter) {
		super(ID, name, pos);
		this.diameter = diameter;
		this.texture = new DynamicTexture(
				TextureGenerator.generateStarshipImage(5, 10), 10, 0);
		selectionIndicator = new SelectionIndicator(
				SelectionIndicator.TYPE_STARSHIP);
	}

	public DynamicTexture getDynamicTexture() {
		return texture;
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		g.drawImage(texture.getImage(), xinpixels, yinpixels, null);
		if (uiSelected)
			selectionIndicator.draw(g, xinpixels, yinpixels);
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

	@Override
	public void setUISelected(boolean selected) {
		uiSelected = selected;
	}

	@Override
	public void update() {
		if (uiSelected)
			selectionIndicator.update();
	}

}
