package stratstuff;

import java.awt.Color;

public class ColorGenerator {

	private static final int PLANET_COLOR_DESERT = 0xFFFF99;
	private static final int PLANET_COLOR_VIVID = 0x1F5C3D;
	private static final int PLANET_COLOR_RED = 0x400000;
	private static final int PLANET_COLOR_DARK = 0x331A00;

	public static int generatePlanetColorCode() {
		int color;
		int type = (int) (Math.random() * 3);

		if (type == 0) {
			color = PLANET_COLOR_DESERT;
		} else if (type == 1) {
			color = PLANET_COLOR_VIVID;
		} else if (type == 2) {
			color = PLANET_COLOR_RED;
		} else {
			color = PLANET_COLOR_DARK;
		}

		ColorData myData = new ColorData(new Color(color), 5, 0);
		int r = myData.getRandomRed();
		int g = myData.getRandomGreen();
		int b = myData.getRandomBlue();
		int a = myData.getRandomAlpha();
		int col = (a << 24) | (r << 16) | (g << 8) | b;

		return col;
	}

}
