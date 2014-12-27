package stratstuff;

import java.util.ArrayList;

public class DebugCommandPrintEdges extends DebugCommand {

	public DebugCommandPrintEdges(DebugConsole console) {
		super(console);
	}

	@Override
	protected boolean checkArguments(ArrayList<String> args) {
		if (args.size() == 3) {
			return true;
		}
		return false;
	}

	@Override
	public void execute(ArrayList<String> args) {
		if (checkArguments(args)) {
			int x = Integer.parseInt(args.get(0));
			int y = Integer.parseInt(args.get(1));
			int z = Integer.parseInt(args.get(2));

			console.print(console.getMain().getWorld().getEdgesString(x, y, z));
		}

		else {
			printErrorMessage();
		}
	}

	@Override
	protected void printErrorMessage() {
		console.print("Usage: edges x y z");
	}

}
