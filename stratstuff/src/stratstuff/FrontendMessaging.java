package stratstuff;

public class FrontendMessaging {

	public static String groundUpdate(int newGroundID, int x, int y, int z) {
		return "0 " + newGroundID + " " + x + " " + y + " " + z;
	}

	public static String objectMovedUpdate(int objectID, int newx, int newy,
			int newz) {
		return "1 " + objectID + " " + newx + " " + newy + " " + newz;
	}

	public static String eventOccurred(int eventID) {
		return "2 " + eventID;
	}

	public static String elementUpdate(int newElementID, int x, int y, int z) {
		return "3 " + newElementID + " " + x + " " + y + " " + z;
	}
}
