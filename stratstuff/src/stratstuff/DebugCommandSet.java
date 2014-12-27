package stratstuff;

import java.util.ArrayList;

public class DebugCommandSet extends DebugCommand {

	public DebugCommandSet(DebugConsole console) {
		super(console);
	}

	@Override
	protected boolean checkArguments(ArrayList<String> args) {
		if (args.size() == 2) {
			if (args.get(0).startsWith("$")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void execute(ArrayList<String> args) {
		if (checkArguments(args)) {
			console.setVariable(args.get(0), args.get(1));
		} else {
			printErrorMessage();
		}
	}

	@Override
	protected void printErrorMessage() {
		console.print("Usage: set $var hello");
	}
}
