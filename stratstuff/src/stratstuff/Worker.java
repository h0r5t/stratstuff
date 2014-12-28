package stratstuff;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Worker extends MovingObject {

	private static final String textureLocation = FileSystem.TEXTURES_DIR
			+ "/units/worker.png";

	public Worker(World world) {
		super(world);
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		g.drawImage(image, xinpixels, yinpixels, GameSettings.TILE_SIZE,
				GameSettings.TILE_SIZE, null);
	}

	@Override
	protected void init() {
		try {
			image = ImageIO.read(new File(textureLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean collides() {
		return false;
	}

	@Override
	public String getType() {
		return "worker";
	}

}
