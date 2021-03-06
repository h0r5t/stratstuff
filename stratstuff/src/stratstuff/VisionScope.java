package stratstuff;

import java.util.ArrayList;

public class VisionScope {

	private Unit unit;

	public VisionScope(Unit unit) {
		this.unit = unit;
	}

	public String getParsedFrontendDataString() {
		World world = unit.getWorld();
		LightManager lightMgr = unit.getWorld().getLightManager();
		MovingObject obj = world.getObjectByUID(unit.getMovingObjUID());
		String frontendData = "";

		ArrayList<WorldPoint> wpList = new ArrayList<WorldPoint>();
		ArrayList<MovingObject> objList = new ArrayList<MovingObject>();

		for (int x = obj.getPosition().getX() - 5; x <= obj.getPosition()
				.getX() + 5; x++) {
			for (int y = obj.getPosition().getY() - 5; y <= obj.getPosition()
					.getY() + 5; y++) {
				WorldPoint wp = world.getWP(x, y, obj.getPosition().getZ());
				if (wp != null) {
					if (!lightMgr.isDarkAt(wp)) {
						wpList.add(wp);
						objList.addAll(wp.getAttachedMovingObjects());
					}
				}

			}
		}

		objList.remove(world.getObjectByUID(unit.getMovingObjUID()));

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
