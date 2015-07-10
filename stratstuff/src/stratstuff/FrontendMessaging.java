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

	public static String eventOccurred(int eventID, String data) {
		return "2 " + eventID + " " + data;
	}

	public static String elementUpdate(int newElementID, int x, int y, int z) {
		return "3 " + newElementID + " " + x + " " + y + " " + z;
	}

	public static String objectSpawnedUpdate(int objectID, int objectType,
			int x, int y, int z) {
		return "4 " + objectID + " " + objectType + " " + x + " " + y + " " + z;
	}

	public static String startRobotWithDesign(int objectID, String designName) {
		return "5 " + designName + " " + objectID;
	}

	public static String removeRobotDesign(int objectID) {
		return "6 " + objectID;
	}
	
	public static String signalReceived(int objectID, Signal signal) {
		return "7 " + objectID + " " + signal.getMessage();
	}

	public static String menuInputMessage(String name, String type,
			String areaData) {
		return "input::" + name + " " + type + " " + areaData;
	}

	public static String shutdownMessage() {
		return "SHUTDOWN";
	}

}
