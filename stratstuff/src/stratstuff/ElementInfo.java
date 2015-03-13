package stratstuff;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ElementInfo {

	private String name;
	private BufferedImage[] images;
	private boolean collides;
	private boolean isLadderDown;
	private boolean isLadderUp;
	private boolean isLightSource;
	private int droppedItemType;

	public String getName() {
		return name;
	}

	public int getDroppedItemType() {
		return droppedItemType;
	}

	public BufferedImage getImage(int imageID) {
		return images[imageID];
	}

	public boolean collides() {
		return collides;
	}

	public ElementInfo(LoadedInfo info) {
		this.name = info.getValueString("name");
		this.collides = info.getValueBool("collides");
		this.isLadderDown = info.getValueBool("ladderdown");
		this.isLadderUp = info.getValueBool("ladderup");
		this.isLightSource = info.getValueBool("lightsource");
		this.droppedItemType = info.getValueInt("drops");
		if (info.getValueString("image") != null) {
			try {
				BufferedImage image = ImageIO.read(new File(
						FileSystem.TEXTURES_ELEMENTS_DIR
								+ info.getValueString("image")));
				images = new BufferedImage[GameSettings.TEXTURE_AMOUNT];
				for (int i = 0; i < images.length; i++) {
					images[i] = image;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		images = new BufferedImage[GameSettings.TEXTURE_AMOUNT];
		TextureShape shape = TextureShape.valueOf(info
				.getValueString("tex_shape"));
		int red = info.getValueInt("tex_red");
		int blue = info.getValueInt("tex_blue");
		int green = info.getValueInt("tex_green");
		int alpha = info.getValueInt("tex_alpha");
		int col = (alpha << 24) | (red << 16) | (green << 8) | blue;
		Color color = new Color(col);
		for (int i = 0; i < images.length; i++) {
			images[i] = TextureGenerator.generateShapedImage20on20(new ColorData(color,
					GameSettings.COLOR_VARIATION, 0), 5, collides, shape);
		}
	}

	public boolean isLadderDown() {
		return isLadderDown;
	}

	public boolean isLadderUp() {
		return isLadderUp;
	}

	public boolean isLightSource() {
		return isLightSource;
	}
}
