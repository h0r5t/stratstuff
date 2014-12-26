package stratstuff;

import javax.swing.JFrame;

public class GameFrame extends JFrame {

	private static String title = "stratstuff v0.1";

	private InputManager inputHandler;

	public GameFrame(InputManager inputHandler) {
		super(title);
		this.inputHandler = inputHandler;
		setSize(GameSettings.FRAME_WIDTH + 10, GameSettings.FRAME_HEIGHT + 20);
		setLocation(GameSettings.FRAME_XPOS, GameSettings.FRAME_YPOS);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
