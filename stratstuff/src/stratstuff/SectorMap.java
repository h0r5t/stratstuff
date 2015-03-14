package stratstuff;

import java.util.ArrayList;
import java.util.Arrays;

public class SectorMap {

	private Sector[][] sectors;

	public SectorMap(int width, int height) {
		sectors = new Sector[width][height];
	}

	public void set(Sector s, int xpos, int ypos) {
		if (xpos < 0) {
			xpos = Math.abs(xpos) * 2 - 1;
		} else if (xpos >= 0) {
			xpos = Math.abs(xpos) * 2;
		}
		if (ypos < 0) {
			ypos = Math.abs(ypos) * 2 - 1;
		} else if (ypos >= 0) {
			ypos = Math.abs(ypos) * 2;
		}
		sectors[xpos][ypos] = s;
	}

	public Sector get(int xpos, int ypos) {
		if (xpos < 0) {
			xpos = Math.abs(xpos) * 2 - 1;
		} else if (xpos >= 0) {
			xpos = Math.abs(xpos) * 2;
		}
		if (ypos < 0) {
			ypos = Math.abs(ypos) * 2 - 1;
		} else if (ypos >= 0) {
			ypos = Math.abs(ypos) * 2;
		}
		return sectors[xpos][ypos];
	}

	public boolean isInBounds(int xpos, int ypos) {
		if (xpos < 0) {
			xpos = Math.abs(xpos) * 2 - 1;
		} else if (xpos >= 0) {
			xpos = Math.abs(xpos) * 2;
		}
		if (ypos < 0) {
			ypos = Math.abs(ypos) * 2 - 1;
		} else if (ypos >= 0) {
			ypos = Math.abs(ypos) * 2;
		}

		if (xpos < sectors.length && ypos < sectors[0].length)
			return true;
		return false;
	}

	public ArrayList<Sector> getAll() {
		ArrayList<Sector> a = new ArrayList<Sector>();

		for (Sector[] array : sectors) {
			a.addAll(Arrays.asList(array));
		}

		return a;
	}

	public void updateAll() {
		for (Sector s : getAll()) {
			if (s != null)
				s.update();
		}
	}

}
