package stratstuff;

import java.util.HashMap;

public class LoadedInfo {

	private HashMap<String, String> infoPairs;

	public LoadedInfo() {
		infoPairs = new HashMap<String, String>();
	}

	public void parse(String line) {
		// should be key = value...
		String[] split = new String[2];
		for (int i = 0; i < split.length; i++) {
			split[i] = line.split("=")[i].trim();
		}

		infoPairs.put(split[0], split[1]);
	}

	public String getValueString(String key) {
		return infoPairs.get(key);
	}

	public int getValueInt(String key) {
		return Integer.parseInt(infoPairs.get(key));
	}

	public boolean getValueBool(String key) {
		return Boolean.parseBoolean(infoPairs.get(key));
	}
}
