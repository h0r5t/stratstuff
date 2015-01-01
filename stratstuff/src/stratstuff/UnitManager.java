package stratstuff;

import java.util.HashMap;

public class UnitManager implements Updatable {

	private Core main;
	private HashMap<Integer, MovingObject> livingObjects;
	private int idCount = 0;

	public UnitManager(Core main) {
		this.main = main;
		livingObjects = new HashMap<Integer, MovingObject>();
	}

	public void addUnit(MovingObject o) {
		livingObjects.put(idCount, o);
		idCount++;
	}

	public MovingObject getUnit(int id) {
		return livingObjects.get(id);
	}

	public HashMap<Integer, MovingObject> getUnits() {
		return livingObjects;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
