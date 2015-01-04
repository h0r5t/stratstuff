package stratstuff;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Texture {

	private ArrayList<BufferedImage> images;
	private int index;

	public Texture(String dirWithImages, boolean random) {
		images = new ArrayList<BufferedImage>();
		File f = new File(dirWithImages);

		for (String child : f.list()) {
			if (child.endsWith("png")) {
				try {
					images.add(ImageIO.read(new File(dirWithImages + "/"
							+ child)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (random) {
			index = (int) (Math.random() * images.size());
		}
	}

	public BufferedImage getImage() {
		// return images.get((int) (Math.random() * images.size()));
		return images.get(index);
	}

	public BufferedImage getImageAt(int i) {
		if (i < images.size()) {
			return images.get(i);
		}
		return images.get(0);
	}
}
