package stratstuff;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class GameFrame extends JFrame {

	private static String title = "stratstuff v0.1";

	private InputManager inputHandler;

	public GameFrame(InputManager inputHandler, GameWindowAdapter windowAdapter) {
		super(title);
		this.inputHandler = inputHandler;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int) screen.getWidth(), (int) screen.getHeight());
		setLocation(GameSettings.GAME_FRAME_XPOS, GameSettings.GAME_FRAME_YPOS);
		addWindowListener(windowAdapter);
	}
}
