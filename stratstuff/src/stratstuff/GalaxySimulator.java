package stratstuff;

public class GalaxySimulator implements Updatable {

	private Galaxy galaxy;

	public GalaxySimulator(Galaxy g) {
		galaxy = g;
	}

	public Galaxy getGalaxy() {
		return galaxy;
	}

	@Override
	public void update() {
		galaxy.update();
	}

}
