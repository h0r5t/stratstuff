package stratstuff;

import java.awt.image.BufferedImage;

public class ElementInfo {

	private String name;
	private BufferedImage image;
	private boolean collides;
	private boolean isLadderDown;
	private boolean isLadderUp;

	public String getName() {
		return name;
	}

	public BufferedImage getImage() {
		return image;
	}

	public boolean collides() {
		return collides;
	}

	public ElementInfo(String name, BufferedImage image, boolean collides,
			boolean isLadderDown, boolean isLadderUp) {
		this.name = name;
		this.image = image;
		this.collides = collides;
		this.isLadderDown = isLadderDown;
		this.isLadderUp = isLadderUp;
	}

	public boolean isLadderDown() {
		return isLadderDown;
	}

	public boolean isLadderUp() {
		return isLadderUp;
	}
}
