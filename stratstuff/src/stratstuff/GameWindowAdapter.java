package stratstuff;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWindowAdapter extends WindowAdapter {
	private Core main;

	public GameWindowAdapter(Core main) {
		this.main = main;
	}

	public void windowClosing(WindowEvent windowEvent) {
		main.save();
		main.tellFrontendToShutdown();
		System.exit(0);
	}
}
