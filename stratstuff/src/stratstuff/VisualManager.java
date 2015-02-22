package stratstuff;

public class VisualManager implements Updatable {

	private GameFrame gameFrame;
	private GameCanvas gameCanvas;
	private boolean canDraw = false;

	public VisualManager(Core core, World world, GameCamera cam,
			InputManager inputHandler, GameCursor cursor,
			GameWindowAdapter windowAdapter, GameMenu menu) {
		initFrame(inputHandler, windowAdapter);
		initCanvas(core, world, inputHandler, cam, cursor, menu);
	}

	private void initCanvas(Core core, World world, InputManager handler,
			GameCamera cam, GameCursor cursor, GameMenu menu) {
		gameCanvas = new GameCanvas(core, world, this, handler, cam, cursor,
				menu);
		gameFrame.add(gameCanvas);
	}

	private void initFrame(InputManager inputHandler,
			GameWindowAdapter windowAdapter) {
		gameFrame = new GameFrame(inputHandler, windowAdapter);
	}

	public void activate() {
		gameFrame.setVisible(true);
		gameCanvas.requestFocus();
		canDraw = true;
	}

	public GameCanvas getCanvas() {
		return gameCanvas;
	}

	public boolean drawNow() {
		return canDraw;
	}

	@Override
	public void update() {
		gameCanvas.repaint();
	}

}
