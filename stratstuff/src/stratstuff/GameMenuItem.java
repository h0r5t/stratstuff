package stratstuff;

public class GameMenuItem {

	private String type;
	private String name;
	private String hotkey;

	public final static String TYPE_SINGLE = "single";
	public final static String TYPE_AREA = "area";

	public GameMenuItem(String type, String hotkey, String name) {
		this.type = type;
		this.name = name;
		this.hotkey = hotkey;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getHotkey() {
		return hotkey;
	}
}
