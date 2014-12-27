package stratstuff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DebugConsole implements Updatable {

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
		commandMap.put("edges", new DebugCommandPrintEdges(this));
		commandMap.put("chg", new DebugCommandChange(this));
	}

	public void commandEntered(String input) {
		String[] s = input.split(" ");
		ArrayList<String> args = new ArrayList<String>(Arrays.asList(Arrays
				.copyOfRange(s, 1, s.length)));
		doCommand(s[0], args);

		frame.print(">");
	}

	private void doCommand(String command, ArrayList<String> arguments) {
		if (command.equals("set") == false && command.equals("get") == false) {
			arguments = insertVariables(arguments);
		}

		if (command.equals("cls")) {
			frame.resetText();
			return;
		}

		if (commandMap.containsKey(command)) {
			commandMap.get(command).execute(arguments);
		} else {
			print(command + " is not a valid command");
		}
		print("\n");
	}

	private ArrayList<String> insertVariables(ArrayList<String> arguments) {
		ArrayList<String> newList = new ArrayList<String>();
		for (String s : arguments) {
			String value = getVariableValue(s);
			if (value != null) {
				if (value.contains(" ")) {
					String[] split = value.split(" ");
					for (String a : split) {
						newList.add(a);
					}

				}

			} else {
				newList.add(s);
			}
		}
		return newList;
	}

	public void setVariable(String var, String value) {
		variablesMap.put(var, value);
	}

	public String getVariableValue(String var) {
		// null if not defined
		return variablesMap.get(var);
	}

	public void print(String s) {
		frame.print(s);
	}

	public Main getMain() {
		return main;
	}

	public void requestFocus() {
		frame.requestFocus();
	}

	@Override
	public void update() {
		String val = main.getCursor().getX() + " " + main.getCursor().getY()
				+ " " + main.getCamera().getLayer();
		variablesMap.put("$cursor", val);
		variablesMap.put("$c", val);
	}

}
