package stratstuff;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GroundInfo {

	private String name;
	private BufferedImage image;
	private boolean collides;

	public String getName() {
		return name;
	}

	public boolean collides() {
		return collides;
	}

	public BufferedImage getImage() {
		return image;
	}

	public GroundInfo(LoadedInfo info) {
		this.name = info.getValueString("name");
		try {
			image = ImageIO.read(new File(FileSystem.TEXTURES_GROUNDS_DIR
					+ info.getValueString("image")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.collides = info.getValueBool("collides");
	}
}
