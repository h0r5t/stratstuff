package stratstuff;

public class FrontendMessaging {

	public static String groundUpdate(int newGroundID, int x, int y, int z) {
		return "0 " + newGroundID + " " + x + " " + y + " " + z;
	}

	public static String objectMovedUpdate(int objectID, int newx, int newy,
			int newz) {
		return "1 " + objectID + " " + newx + " " + newy + " " + newz;
	}
}
