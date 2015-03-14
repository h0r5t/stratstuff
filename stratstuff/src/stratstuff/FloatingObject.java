package stratstuff;

import java.awt.Graphics2D;

public abstract class FloatingObject implements Updatable, Drawable, Saveable {

	protected int ID;
	protected String name;
	protected SpacePosition position;

	public static final int ID_PLANET = 0;
	public static final int ID_STARSHIP = 1;

	public FloatingObject(int ID, String name, SpacePosition pos) {
		this.ID = ID;
		this.name = name;
		setPosition(pos);
	}

	public int getID() {
		return ID;
	}

	public void setPosition(SpacePosition position) {
		this.position = position;
	}

	public SpacePosition getPosition() {
		return position;
	}

	@Override
	public abstract void draw(Graphics2D g, int xinpixels, int yinpixels);

	public static FloatingObject createFromSave(String line, Sector sector) {
		FloatingObject obj = null;

		String[] split = line.split(" ");
		int objID = Integer.parseInt(split[0]);

		if (objID == 0) {
			int uniqueID = Integer.parseInt(split[1]);
			double x = Double.parseDouble(split[2]);
			double y = Double.parseDouble(split[3]);
			String name = split[4];
			int colorCode = Integer.parseInt(split[5]);
			int diameter = Integer.parseInt(split[6]);

			obj = new Planet(uniqueID, name, new SpacePosition(sector, x, y),
					colorCode, diameter);
			UniqueIDFactory.increment();
		}

		else if (objID == 1) {
			int uniqueID = Integer.parseInt(split[1]);
			double x = Double.parseDouble(split[2]);
			double y = Double.parseDouble(split[3]);
			String name = split[4];
			int diameter = Integer.parseInt(split[5]);

			obj = new Starship(uniqueID, name, new SpacePosition(sector, x, y),
					diameter);
		}

		return obj;
	}
}
