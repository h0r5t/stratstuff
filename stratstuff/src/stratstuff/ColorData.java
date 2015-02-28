package stratstuff;

import java.awt.Color;

public class ColorData {

	private int rmin, rmax, gmin, gmax, bmin, bmax, alphamin, alphamax;

	public ColorData(Color c, int colorVariation, int alphaVariation) {
		rmin = c.getRed() - colorVariation;
		rmax = c.getRed() + colorVariation;
		gmin = c.getGreen() - colorVariation;
		gmax = c.getGreen() + colorVariation;
		bmin = c.getBlue() - colorVariation;
		bmax = c.getBlue() + colorVariation;
		alphamin = c.getAlpha() - alphaVariation;
		alphamax = c.getAlpha() + alphaVariation;
	}

	public int getRandomRed() {
		int r = rmin + (int) (Math.random() * (rmax - rmin));
		if (r > 255)
			r = 255;
		else if (r < 0)
			r = 0;
		return r;
	}

	public int getRandomBlue() {
		int b = bmin + (int) (Math.random() * (bmax - bmin));
		if (b > 255)
			b = 255;
		else if (b < 0)
			b = 0;
		return b;
	}

	public int getRandomGreen() {
		int g = gmin + (int) (Math.random() * (gmax - gmin));
		if (g > 255)
			g = 255;
		else if (g < 0)
			g = 0;
		return g;
	}

	public int getRandomAlpha() {
		int a = alphamin + (int) (Math.random() * (alphamax - alphamin));
		if (a > 255)
			a = 255;
		else if (a < 0)
			a = 0;
		return a;
	}
}
