package stratstuff;

import java.util.ArrayList;

public class DebugCommandMoveTask extends DebugCommand {

	public DebugCommandMoveTask(DebugConsole console) {
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
		if (checkArguments(args) == false) {
			printErrorMessage();
			return;
		}

		TaskManager mgr = console.getMain().getTaskManager();
		int unitID = Integer.parseInt(args.get(0));
		int x = Integer.parseInt(args.get(1));
		int y = Integer.parseInt(args.get(2));
		int z = Integer.parseInt(args.get(3));

		MovingObject o = console.getMain().getObjectManager().getUnit(unitID);
		MoveTask task = new MoveTask(console.getMain(), o, console.getMain()
				.getWorld().getWP(x, y, z));
		mgr.runTask(task);
	}

	@Override
	protected void printErrorMessage() {
		System.out.println("Usage: movetask unitID x y z");
	}

}
