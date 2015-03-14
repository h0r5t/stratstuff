package stratstuff;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class SelectionMenu implements Drawable {

	private ArrayList<Starship> selectedStarships;

	public SelectionMenu() {
		selectedStarships = new ArrayList<Starship>();
	}

	public ArrayList<Starship> getSelectedStarships() {
		return selectedStarships;
	}

	public void setSelectedStarships(ArrayList<Starship> list) {
		for (Starship s : selectedStarships) {
			s.setUISelected(false);
		}
		selectedStarships = list;
		for (Starship s : selectedStarships) {
			s.setUISelected(true);
		}
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
	}

}
