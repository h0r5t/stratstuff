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

	public MovingObject getObject(int id) {
		return movingObjects.get(id);
	}

	public HashMap<Integer, MovingObject> getUnits() {
		return movingObjects;
	}

	public void removeUnit(int uniqueID) {
		movingObjects.remove(uniqueID);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
