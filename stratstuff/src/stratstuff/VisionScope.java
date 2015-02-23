package stratstuff;

import java.util.ArrayList;

public class VisionScope {

	private Core core;
	private Unit unit;

	public VisionScope(Core core, Unit unit) {
		this.core = core;
		this.unit = unit;
	}

	public String getParsedFrontendDataString() {
		World w = core.getWorld();
		LightManager lightMgr = core.getLightManager();
		MovingObject obj = w.getObjectByUID(unit.getMovingObjUID());
		String frontendData = "";

		ArrayList<WorldPoint> wpList = new ArrayList<WorldPoint>();
		ArrayList<MovingObject> objList = new ArrayList<MovingObject>();

		for (int x = obj.getPosition().getX() - 5; x <= obj.getPosition()
				.getX() + 5; x++) {
			for (int y = obj.getPosition().getY() - 5; y <= obj.getPosition()
					.getY() + 5; y++) {
				WorldPoint wp = w.getWP(x, y, obj.getPosition().getZ());
				if (!lightMgr.isDarkAt(wp)) {
					wpList.add(wp);
					objList.addAll(wp.getAttachedMovingObjects());
				}
			}
		}

		objList.remove(core.getWorld().getObjectByUID(unit.getMovingObjUID()));

		for (WorldPoint wp : wpList) {
			frontendData += "&wp:" + wp.getX() + ":" + wp.getY() + ":"
					+ wp.getZ() + ":" + wp.getGround() + ":"
					+ wp.getAttachedElement();

		}

		for (MovingObject o : objList) {
			frontendData += "&obj:" + o.getUniqueID() + ":" + o.getTypeInt()
					+ ":" + o.getPosition().getX() + ":"
					+ o.getPosition().getY() + ":" + o.getPosition().getZ();
		}

		return frontendData.replaceFirst("&", "");
	}
}
