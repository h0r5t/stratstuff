package stratstuff;

import java.util.ArrayList;
import java.util.HashMap;

public class UnitManager implements Updatable {

	private Core core;
	private HashMap<Integer, Unit> unitMap;

	public UnitManager(Core core) {
		this.core = core;
		unitMap = new HashMap<Integer, Unit>();
	}

	public void addUnit(Unit u) {
		unitMap.put(u.getUniqueID(), u);
	}

	public Unit getUnit(int uid) {
		return unitMap.get(uid);
	}

	public void initItemsInFrontend() {
		for (Unit unit : getUnitList()) {
			unit.initItemsInFrontend();
		}
	}

	@Override
	public void update() {

	}

	public ArrayList<Unit> getUnitList() {
		return new ArrayList<Unit>(unitMap.values());
	}

	public Unit getUnitByObjectID(int objUID) {
		for (Unit u : getUnitList()) {
			if (u.getMovingObjUID() == objUID)
				return u;
		}

		return null;
	}

	public void registerRobots() {
		for (Unit u : unitMap.values()) {
			u.registerRobot();
		}
	}

}
