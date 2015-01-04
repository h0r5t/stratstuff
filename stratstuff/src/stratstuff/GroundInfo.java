package stratstuff;

import java.awt.image.BufferedImage;

public class GroundInfo {

	private String name;
	private Texture texture;
	private boolean collides;

	public String getName() {
		return name;
	}

	public BufferedImage getImage() {
		return texture.getImage();
	}

	public boolean collides() {
		return collides;
	}

	public GroundInfo(String name, String imageDir, boolean randomTexture,
			boolean collides) {
		this.name = name;
		texture = new Texture(imageDir, randomTexture);
		this.collides = collides;
	}
}
