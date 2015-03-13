package stratstuff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TextureGenerator {

	public static BufferedImage generateStarshipImage(int width, int height) {
		BufferedImage image = new BufferedImage(width + height, width + height,
				BufferedImage.TYPE_INT_ARGB);

		Graphics g = image.getGraphics();

		g.setColor(Color.GRAY);
		g.fillRect(image.getWidth() / 2 - width / 2, image.getHeight() / 2
				- height / 2, width, height);

		return image;
	}

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
				new Color(colorCode), 20, 0), 5, false, diameter, diameter);

		image = add3DEffect(image);

		return image;
	}

	private static BufferedImage add3DEffect(BufferedImage image) {

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				Color color = new Color(image.getRGB(x, y), true);
				int a = color.getAlpha();
				if (a > 0) {
					int r = color.getRed();
					int b = color.getBlue();
					int g = color.getGreen();

					if (x != 0) {
						int xf = (int) (((double) x / (double) image.getWidth()) * 100);
						if (xf > 50) {
							int n = (100 - xf);
							n /= 8;
							n++;

							r -= xf / n;
							b -= xf / n;
							g -= xf / n;
						}
					}

					if (y != 0) {
						int yf = (int) (((double) y / (double) image
								.getHeight()) * 100);
						if (yf > 50) {
							int n = (100 - yf);
							n /= 8;
							n++;

							r -= yf / n;
							b -= yf / n;
							g -= yf / n;
						}
					}

					if (r < 0)
						r = 0;
					if (b < 0)
						b = 0;
					if (g < 0)
						g = 0;
					int col = (a << 24) | (r << 16) | (g << 8) | b;
					image.setRGB(x, y, col);
				}

			}
		}

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
