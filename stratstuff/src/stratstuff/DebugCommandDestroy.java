package stratstuff;

import java.util.ArrayList;

public class DebugCommandDestroy extends DebugCommand {
	public DebugCommandDestroy(DebugConsole console) {
		super(console);
	}

	@Override
	protected boolean checkArguments(ArrayList<String> args) {
		if (args.size() == 4) {
			return true;
		}
		return false;
	}

	@Override
	public void execute(ArrayList<String> args) {
		if (!checkArguments(args)) {
			printErrorMessage();
			return;
		}

		int x = Integer.parseInt(args.get(1));
		int y = Integer.parseInt(args.get(2));
		int z = Integer.parseInt(args.get(3));
		int elementID = Integer.parseInt(args.get(0));

		console.getMain()
				.getWorld()
				.removeElementFromWP(
						console.getMain().getWorld().getWP(x, y, z), elementID);
	}

	@Override
	protected void printErrorMessage() {
		console.print("Usage: destroy elementID x y z");
	}
}
