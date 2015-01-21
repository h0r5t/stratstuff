package stratstuff;

public class VisualManager implements Updatable {

	private GameFrame gameFrame;
	private GameCanvas gameCanvas;
	private boolean canDraw = false;

	public VisualManager(World world, GameCamera cam,
			InputManager inputHandler, GameCursor cursor,
			GameWindowAdapter windowAdapter, GameMenu menu) {
		initFrame(inputHandler, windowAdapter);
		initCanvas(world, inputHandler, cam, cursor, menu);
	}

	private void initCanvas(World world, InputManager handler, GameCamera cam,
			GameCursor cursor, GameMenu menu) {
		gameCanvas = new GameCanvas(world, this, handler, cam, cursor, menu);
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

	public boolean drawNow() {
		return canDraw;
	}

	@Override
	public void update() {
		gameCanvas.repaint();
	}

}
