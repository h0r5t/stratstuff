package stratstuff;

import java.util.ArrayList;

public abstract class DebugCommand {

	protected DebugConsole console;

	public DebugCommand(DebugConsole console) {
		this.console = console;
	}

	protected abstract boolean checkArguments(ArrayList<String> args);

	public abstract void execute(ArrayList<String> args);

	protected abstract void printErrorMessage();

}
