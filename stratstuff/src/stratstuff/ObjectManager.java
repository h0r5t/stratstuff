package stratstuff;

import java.util.HashMap;

public class ObjectManager implements Updatable {
	private HashMap<Integer, MovingObject> movingObjects;

	public ObjectManager(Core main) {
		movingObjects = new HashMap<Integer, MovingObject>();
	}

	public void addUnit(MovingObject o) {
		movingObjects.put(o.getUniqueID(), o);
	}

	public MovingObject getUnit(int id) {
		return movingObjects.get(id);
	}

	public HashMap<Integer, MovingObject> getUnits() {
		return movingObjects;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
