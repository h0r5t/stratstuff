package stratstuff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TextureGenerator {

	public static BufferedImage generateSectorBackground() {
		BufferedImage image = new BufferedImage(Sector.sectorWidth,
				Sector.sectorHeight, BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());

		for (int i = 0; i < 50; i++) {
			int x = (int) (Math.random() * image.getWidth());
			int y = (int) (Math.random() * image.getHeight());

			image.setRGB(x, y, Color.WHITE.getRGB());
		}

		return image;
	}

	public static BufferedImage generatePlanetImage(int colorCode, int diameter) {
		BufferedImage image = generateCircle(new ColorData(
				new Color(colorCode), GameSettings.COLOR_VARIATION, 0), 5,
				false, diameter, diameter);

		return image;
	}

	public static BufferedImage generateShapedImage20on20(ColorData myData,
			int sectionSize, boolean blackBorder, TextureShape shape) {
		if (shape == TextureShape.rectangle) {
			return generateRectangle(myData, sectionSize, blackBorder, 20, 20);
		} else if (shape == TextureShape.circle) {
			return generateCircle(myData, sectionSize, blackBorder, 20, 20);
		} else
			return null;
	}

	private static BufferedImage generateRectangle(ColorData myData,
			int sectionSize, boolean blackBorder, int width, int height) {
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics graphics = image.getGraphics();
		for (int x = 0; x < image.getWidth(); x += sectionSize) {
			for (int y = 0; y < image.getHeight(); y += sectionSize) {
				int r = myData.getRandomRed();
				int g = myData.getRandomGreen();
				int b = myData.getRandomBlue();
				int a = myData.getRandomAlpha();
				int col = (a << 24) | (r << 16) | (g << 8) | b;
				graphics.setColor(new Color(col));
				graphics.fillRect(x, y, sectionSize, sectionSize);
			}
		}

		if (blackBorder) {
			graphics.setColor(Color.BLACK);
			graphics.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);
		}

		return image;
	}

	private static BufferedImage generateCircle(ColorData myData,
			int sectionSize, boolean blackBorder, int width, int height) {
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics graphics = image.getGraphics();

		Color c = new Color(0, 0, 0, 0);
		graphics.setColor(c);
		graphics.fillRect(0, 0, width, height);

		int r = myData.getRandomRed();
		int g = myData.getRandomGreen();
		int b = myData.getRandomBlue();
		int a = myData.getRandomAlpha();
		int col = (a << 24) | (r << 16) | (g << 8) | b;
		graphics.setColor(new Color(col));
		graphics.fillOval(0, 0, width, height);

		if (blackBorder) {
			graphics.setColor(Color.BLACK);
			graphics.drawOval(0, 0, image.getWidth() - 1, image.getHeight() - 1);
		}

		return image;
	}
}
