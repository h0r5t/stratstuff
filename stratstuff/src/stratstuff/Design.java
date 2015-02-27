package stratstuff;

import java.io.File;

public class Design {

	private String name;
	private File file;

	public Design(String name, File file) {
		this.name = name;
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return file.toString();
	}
}
