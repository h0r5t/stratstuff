package stratstuff;

public class VisualManager implements Updatable {

	private GameFrame gameFrame;
	private GamePanel gamePanel;
	private boolean canDraw = false;

	public VisualManager(Core core, World world, GameCamera cam,
			InputManager inputHandler, GameCursor cursor,
			GameWindowAdapter windowAdapter, GameMenu menu) {
		initFrame(inputHandler, windowAdapter);
		initPanel(core, world, inputHandler, cam, cursor, menu);
	}

	private void initPanel(Core core, World world, InputManager handler,
			GameCamera cam, GameCursor cursor, GameMenu menu) {
		gamePanel = new GamePanel(core, world, this, handler, cam, cursor,
				menu);
		gameFrame.add(gamePanel);
	}

	private void initFrame(InputManager inputHandler,
			GameWindowAdapter windowAdapter) {
		gameFrame = new GameFrame(inputHandler, windowAdapter);
	}

	public void activate() {
		gameFrame.setVisible(true);
		gamePanel.requestFocus();
		canDraw = true;
	}

	public GamePanel getCanvas() {
		return gamePanel;
	}

	public boolean drawNow() {
		return canDraw;
	}

	@Override
	public void update() {
		gamePanel.repaint();
	}

}
