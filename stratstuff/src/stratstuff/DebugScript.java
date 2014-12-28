package stratstuff;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DebugScript {

	private ArrayList<String> commands;
	private DebugConsole console;
	private String scriptName;

	public DebugScript(DebugConsole console, String scriptName) {
		this.console = console;
		this.scriptName = scriptName;
		try {
			loadScript();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void loadScript() throws FileNotFoundException {
		commands = new ArrayList<String>();
		Scanner scanner = new Scanner(new File(FileSystem.SCRIPTS_DIR + "/"
				+ scriptName + ".script"));

		while (scanner.hasNextLine()) {
			commands.add(scanner.nextLine().trim());
		}
	}

	public void execute() {
		console.print("%%% " + scriptName + ".script %%%%%%%\n");

		for (String c : commands) {
			console.print("        " + c);
			console.commandEntered(true, c);
		}

		console.print("%%%%%%%%%%%%%%%%%%\n");
	}
}
