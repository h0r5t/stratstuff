package stratstuff;

import java.awt.image.BufferedImage;

public class ElementInfo {

	private String name;
	private BufferedImage image;
	private boolean collides;

	public String getName() {
		return name;
	}

	public BufferedImage getImage() {
		return image;
	}

	public boolean collides() {
		return collides;
	}

	public ElementInfo(String name, BufferedImage image, boolean collides) {
		this.name = name;
		this.image = image;
		this.collides = collides;
	}
}
