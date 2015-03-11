package stratstuff;

import java.awt.Graphics2D;

public class GalaxyCamera {

	private Galaxy galaxy;

	private int shiftX = 0;
	private int shiftY = 0;

	public GalaxyCamera(Galaxy galaxy) {
		this.galaxy = galaxy;
	}

	public SpacePosition screenPosToSpacePos(int screenX, int screenY) {
		int sectorX, sectorY, microX, microY;

		if (shiftX < 0 && screenX < Math.abs(shiftX)) {
			sectorX = -(Sector.sectorWidth - shiftX - screenX)
					/ Sector.sectorWidth;
			microX = -(sectorX * Sector.sectorWidth) + shiftX + screenX;
		} else {
			sectorX = (shiftX + screenX) / Sector.sectorWidth;
			microX = screenX + shiftX - (sectorX * Sector.sectorWidth);
		}
		if (shiftY < 0 && screenY < Math.abs(shiftY)) {
			sectorY = -(Sector.sectorHeight - shiftY - screenY)
					/ Sector.sectorHeight;
			microY = -(sectorY * Sector.sectorHeight) + shiftY + screenY;
		} else {
			sectorY = (shiftY + screenY) / Sector.sectorHeight;
			microY = screenY + shiftY - (sectorY * Sector.sectorHeight);
		}

		return new SpacePosition(galaxy.getSector(sectorX, sectorY), microX,
				microY);
	}

	public void draw(Graphics2D g) {
		int sectorX, sectorY;

		if (shiftX < 0) {
			sectorX = -(Sector.sectorWidth - shiftX) / Sector.sectorWidth;
		} else {
			sectorX = (shiftX) / Sector.sectorWidth;
		}
		if (shiftY < 0) {
			sectorY = -(Sector.sectorHeight - shiftY) / Sector.sectorHeight;
		} else {
			sectorY = (shiftY) / Sector.sectorHeight;
		}

		for (int i = -1; i < 2; i++) {
			for (int o = -1; o < 2; o++) {

				int a = sectorX + i;
				int b = sectorY + o;

				int x = a * Sector.sectorWidth - shiftX;
				int y = b * Sector.sectorHeight - shiftY;

				if (galaxy.isInBounds(a, b))
					galaxy.getSector(a, b).draw(g, x, y);
			}
		}
	}

	public void up() {
		shiftY -= 20;
	}

	public void down() {
		shiftY += 20;
	}

	public void right() {
		shiftX += 20;
	}

	public void left() {
		shiftX -= 20;
	}
}
