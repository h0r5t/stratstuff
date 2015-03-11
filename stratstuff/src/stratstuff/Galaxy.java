package stratstuff;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class Galaxy implements Drawable, Updatable {

	private String name;
	private GalaxyCamera camera;
	private SectorMap sectorMap;

	private int galaxyWidth, galaxyHeight;

	public Galaxy(String name, int galaxyWidth, int galaxyHeight) {
		this.galaxyWidth = galaxyWidth;
		this.galaxyHeight = galaxyHeight;
		this.name = name;
		sectorMap = new SectorMap(galaxyWidth, galaxyHeight);

		camera = new GalaxyCamera(this);
	}

	public int getGalaxyWidth() {
		return galaxyWidth;
	}

	public int getGalaxyHeight() {
		return galaxyHeight;
	}

	public boolean isInBounds(int sectorX, int sectorY) {
		return sectorMap.isInBounds(sectorX, sectorY);
	}

	public String getName() {
		return name;
	}

	public void setSector(Sector sector) {
		sectorMap.set(sector, sector.getXPos(), sector.getYPos());
	}

	public Sector getSector(int x, int y) {
		return sectorMap.get(x, y);
	}

	public ArrayList<Sector> getAllSectors() {
		return sectorMap.getAll();
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		camera.draw(g);
	}

	@Override
	public void update() {

	}

	public GalaxyCamera getCamera() {
		return camera;
	}

}
