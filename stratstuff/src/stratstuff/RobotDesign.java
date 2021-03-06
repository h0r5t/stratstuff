package stratstuff;

import java.io.File;

public class RobotDesign {

	private String name;
	private File file;

	public RobotDesign(String name, File file) {
		this.name = name;
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return file.toString();
	}

	public File getFile() {
		return file;
	}
}
