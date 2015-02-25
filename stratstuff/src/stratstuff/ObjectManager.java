package stratstuff;

import java.util.ArrayList;
import java.util.HashMap;

public class ObjectManager implements Updatable {
	private HashMap<Integer, MovingObject> movingObjects;
	private ArrayList<MicroObject> microObjects;
	private ArrayList<MicroObject> toDelete;

	public ObjectManager(Core main) {
		movingObjects = new HashMap<Integer, MovingObject>();
		microObjects = new ArrayList<MicroObject>();
		toDelete = new ArrayList<MicroObject>();
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

	public void removeObject(int uniqueID) {
		movingObjects.remove(uniqueID);
	}

	@Override
	public void update() {
		for (MovingObject o : movingObjects.values()) {
			o.update();
		}
		for (MicroObject o : microObjects) {
			o.update();
		}
		for (MicroObject o : toDelete) {
			microObjects.remove(o);
		}
		toDelete.clear();
	}

	public void addMicroObject(MicroObject object) {
		microObjects.add(object);
	}

	public void removeMicroObject(MicroObject o) {
		toDelete.add(o);
	}
}
