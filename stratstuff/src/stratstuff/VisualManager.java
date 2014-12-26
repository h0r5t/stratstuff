package stratstuff;

public class VisualManager implements Updatable {

	private GameFrame gameFrame;
	private GameCanvas gameCanvas;

	public VisualManager(World world, GameCamera cam,
			InputManager inputHandler, GameCursor cursor) {
		initFrame(inputHandler);
		initCanvas(world, inputHandler, cam, cursor);
	}

	private void initCanvas(World world, InputManager handler, GameCamera cam,
			GameCursor cursor) {
		gameCanvas = new GameCanvas(world, handler, cam, cursor);
		gameFrame.add(gameCanvas);
	}

	private void initFrame(InputManager inputHandler) {
		gameFrame = new GameFrame(inputHandler);
	}

	public void activate() {
		gameFrame.setVisible(true);
		gameCanvas.requestFocus();
	}

	@Override
	public void update() {
		gameCanvas.repaint();
	}

}
