package stratstuff;

import java.util.ArrayList;

public class DebugCommandGet extends DebugCommand {

	public DebugCommandGet(DebugConsole console) {
		super(console);
	}

	@Override
	protected boolean checkArguments(ArrayList<String> args) {
		if (args.get(0).startsWith("$")) {
			return true;
		}
		return false;
	}

	@Override
	public void execute(ArrayList<String> args) {
		if (checkArguments(args)) {
			String value = console.getVariableValue(args.get(0));
			console.print(value);
		} else {
			printErrorMessage();
		}
	}

	@Override
	protected void printErrorMessage() {
		console.print("Usage: get $var");
	}

}
