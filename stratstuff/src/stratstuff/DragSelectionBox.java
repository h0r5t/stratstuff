package stratstuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class DragSelectionBox implements Drawable {

	private Galaxy galaxy;
	private int x, y;
	private int endX, endY;

	public DragSelectionBox(Galaxy g, int startX, int startY) {
		galaxy = g;
		x = startX;
		y = startY;
	}

	public void setEndX(int x) {
		endX = x;
	}

	public void setEndY(int y) {
		endY = y;
	}

	public ArrayList<Starship> getAllSelectedStarships() {
		// Bug: selector only between max 2 sectors.
		SpacePosition pos1 = galaxy.getCamera().screenPosToSpacePos(x, y);
		SpacePosition pos2 = galaxy.getCamera().screenPosToSpacePos(endX, endY);

		ArrayList<Starship> ships = new ArrayList<Starship>();

		if (pos1.getSector().equals(pos2.getSector())) {
			ships.addAll(pos1.getSector().getAllStarships());
		} else {
			ships.addAll(pos1.getSector().getAllStarships());
			ships.addAll(pos2.getSector().getAllStarships());
		}

		ArrayList<Starship> toDelete = new ArrayList<Starship>();
		for (Starship ship : ships) {
			double macX = ship.getPosition().getMacX();
			double macY = ship.getPosition().getMacY();

			if (macX < pos1.getMacX() || macY < pos1.getMacY()
					|| macX > pos2.getMacX() || macY > pos2.getMacY()) {
				toDelete.add(ship);
			}
		}

		for (Starship ship : toDelete) {
			ships.remove(ship);
		}

		return ships;
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		g.setColor(Color.ORANGE);
		g.drawRect(x, y, endX - x, endY - y);
	}

	public boolean isBigEnough() {
		if (endX - x > 5 || endY - y > 5)
			return true;
		return false;
	}

}
