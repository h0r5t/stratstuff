package stratstuff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GeneratedTexture implements Drawable {

	private BufferedImage image;
	private GeneratedTextureData myData;

	public GeneratedTexture(GeneratedTextureData data) {
		myData = data;
		generateImage();
	}

	private void generateImage() {

		image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = image.getGraphics();
		for (int x = 0; x < image.getWidth(); x += 10) {
			for (int y = 0; y < image.getHeight(); y += 10) {
				int r = myData.getRandomRed();
				int g = myData.getRandomGreen();
				int b = myData.getRandomBlue();
				int a = myData.getRandomAlpha();
				int col = (a << 24) | (r << 16) | (g << 8) | b;
				graphics.setColor(new Color(col));
				graphics.fillRect(x, y, x + 10, y + 10);
			}
		}
	}

	public BufferedImage getImage() {
		return image;
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		g.drawImage(image, xinpixels, yinpixels, GameSettings.TILE_SIZE,
				GameSettings.TILE_SIZE, null);
	}
}
