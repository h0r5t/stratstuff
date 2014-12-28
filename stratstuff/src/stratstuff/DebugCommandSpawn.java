package stratstuff;

import java.util.ArrayList;

public class DebugCommandSpawn extends DebugCommand {

	public DebugCommandSpawn(DebugConsole console) {
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
		if (checkArguments(args)) {
			World w = console.getMain().getWorld();
			int unitID = Integer.parseInt(args.get(0));
			int x = Integer.parseInt(args.get(1));
			int y = Integer.parseInt(args.get(2));
			int z = Integer.parseInt(args.get(3));

			if (unitID == 0) {
				w.spawnObject(new Worker(w), w.getWP(x, y, z));
			}
		} else {
			printErrorMessage();
		}
	}

	@Override
	protected void printErrorMessage() {
		console.print("Usage: spawn unitID x y z");
	}

}
