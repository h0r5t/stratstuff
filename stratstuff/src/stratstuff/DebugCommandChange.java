package stratstuff;

import java.util.ArrayList;

public class DebugCommandChange extends DebugCommand {

	public DebugCommandChange(DebugConsole console) {
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
		int x = Integer.parseInt(args.get(0));
		int y = Integer.parseInt(args.get(1));
		int z = Integer.parseInt(args.get(2));
		int newID = Integer.parseInt(args.get(3));

		console.getMain()
				.getWorld()
				.changeGround(console.getMain().getWorld().getWP(x, y, z),
						newID);
	}

	@Override
	protected void printErrorMessage() {
		console.print("Usage: chg x y z id");
	}

}
