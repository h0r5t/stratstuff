package stratstuff;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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

	public ElementInfo(LoadedInfo info) {
		this.name = info.getValueString("name");
		try {
			this.image = ImageIO.read(new File(FileSystem.TEXTURES_ELEMENTS_DIR
					+ info.getValueString("image")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.collides = info.getValueBool("collides");
		this.isLadderDown = info.getValueBool("ladderdown");
		this.isLadderUp = info.getValueBool("ladderup");
	}

	public boolean isLadderDown() {
		return isLadderDown;
	}

	public boolean isLadderUp() {
		return isLadderUp;
	}
}
