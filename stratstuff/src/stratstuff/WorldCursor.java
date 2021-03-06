package stratstuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class WorldCursor implements Drawable {

	private int x;
	private int y;
	private BufferedImage image;

	private WorldCamera camera;
	private static final int box_length = 150;
	private static final int box_height = 30;

	public WorldCursor(WorldCamera camera) {
		this.camera = camera;
		camera.setCursor(this);
		x = 10;
		y = 10;
		try {
			image = ImageIO.read(new File(FileSystem.TEXTURES_DIR
					+ "/ui/cursor.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void moveRight() {
		if (x < camera.getWorld().getWidth() - 1)
			x++;
	}

	public void moveLeft() {
		if (x > 0)
			x--;
	}

	public void moveDown() {
		if (y < camera.getWorld().getHeight() - 1)
			y++;
	}

	public void moveUp() {
		if (y > 0)
			y--;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		g.drawImage(image, xinpixels, yinpixels, GameSettings.TILE_SIZE,
				GameSettings.TILE_SIZE, null);

		g.setColor(Color.BLACK);
		g.fillRect(GameSettings.GAME_FRAME_WIDTH - box_length,
				GameSettings.GAME_FRAME_HEIGHT - box_height, box_length,
				box_height);

		String text = "X=" + x + " Y=" + y + " Z=" + camera.getLayer();
		g.setColor(Color.WHITE);
		g.drawString(text, GameSettings.GAME_FRAME_WIDTH - box_length,
				GameSettings.GAME_FRAME_HEIGHT - box_height + 20);

	}
}
