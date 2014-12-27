package stratstuff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DebugConsole {

	private Main main;
	private DebugConsoleFrame frame;
	private HashMap<String, DebugCommand> commandMap;
	private HashMap<String, String> variablesMap;

	public DebugConsole(Main main) {
		this.main = main;
		frame = new DebugConsoleFrame(this);
		frame.setVisible(true);
		frame.print(">");
		frame.setInitialCaret();

		initCommands();
		variablesMap = new HashMap<String, String>();
	}

	private void initCommands() {
		commandMap = new HashMap<String, DebugCommand>();
		commandMap.put("set", new DebugCommandSet(this));
		commandMap.put("get", new DebugCommandGet(this));
	}

	public void commandEntered(String input) {
		String[] s = input.split(" ");
		ArrayList<String> args = new ArrayList<String>(Arrays.asList(Arrays
				.copyOfRange(s, 1, s.length)));
		doCommand(s[0], args);

		frame.print(">");
	}

	private void doCommand(String command, ArrayList<String> arguments) {
		if (commandMap.containsKey(command)) {
			commandMap.get(command).execute(arguments);
		} else {
			print(command + " is not a valid command");
		}
		print("\n");
	}

	public void setVariable(String var, String value) {
		variablesMap.put(var, value);
	}

	public String getVariableValue(String var) {
		if (var.equals("$cursor")) {
			String val = main.getCursor().getX() + ","
					+ main.getCursor().getY() + ","
					+ main.getCamera().getLayer();
			return val;
		}
		// null if not defined
		return variablesMap.get(var);
	}

	public void print(String s) {
		frame.print(s);
	}

	public void requestFocus() {
		frame.requestFocus();
	}

}
