package stratstuff;

import java.util.ArrayList;
import java.util.HashMap;

public class DebugCommandListUnits extends DebugCommand {

	public DebugCommandListUnits(DebugConsole console) {
		super(console);
	}

	@Override
	protected boolean checkArguments(ArrayList<String> args) {
		return true;
	}

	@Override
	public void execute(ArrayList<String> args) {
		HashMap<Integer, MovingObject> units = console.getMain()
				.getObjectManager().getUnits();

		for (Integer i : units.keySet()) {
			MovingObject o = units.get(i);
			console.print(i + " : " + o.getName() + " "
					+ o.getPosition().getX() + " " + o.getPosition().getY()
					+ " " + o.getPosition().getZ());
			console.print("\n");
		}
	}

	@Override
	protected void printErrorMessage() {

	}

}
