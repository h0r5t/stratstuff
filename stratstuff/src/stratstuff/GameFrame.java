package stratstuff;

import javax.swing.JFrame;

public class GameFrame extends JFrame {

	private static String title = "stratstuff v0.1";

	private InputManager inputHandler;

	public GameFrame(InputManager inputHandler, GameWindowAdapter windowAdapter) {
		super(title);
		this.inputHandler = inputHandler;
		setSize(GameSettings.GAME_FRAME_WIDTH + 10,
				GameSettings.GAME_FRAME_HEIGHT + 20);
		setLocation(GameSettings.GAME_FRAME_XPOS, GameSettings.GAME_FRAME_YPOS);
		addWindowListener(windowAdapter);
	}
}
