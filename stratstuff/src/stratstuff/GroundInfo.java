package stratstuff;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class GroundInfo {

	private String name;
	private boolean collides;
	private BufferedImage[] images;

	public String getName() {
		return name;
	}

	public boolean collides() {
		return collides;
	}

	public BufferedImage getImage(int imageID) {
		return images[imageID];
	}

	public GroundInfo(LoadedInfo info) {
		this.name = info.getValueString("name");
		this.collides = info.getValueBool("collides");
		images = new BufferedImage[GameSettings.TEXTURE_AMOUNT];
		int red = info.getValueInt("tex_red");
		int blue = info.getValueInt("tex_blue");
		int green = info.getValueInt("tex_green");
		int alpha = info.getValueInt("tex_alpha");

		// int r = 5;
		// int g = 74;
		// int b = 14;
		// int a = 255;
		int col = (alpha << 24) | (red << 16) | (green << 8) | blue;
		Color color = new Color(col);
		for (int i = 0; i < images.length; i++) {
			images[i] = TextureGenerator.generateShapedImage20on20(new ColorAttributes(color,
					GameSettings.COLOR_VARIATION, 0), 10, false,
					TextureShape.rectangle);
		}
	}
}
