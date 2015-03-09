package stratstuff;

public class VisualManager implements Updatable {

	private GameFrame gameFrame;
	private GamePanel gamePanel;
	private boolean canDraw = false;
	private World renderedWorld;

	public VisualManager(Core core, InputManager inputHandler,
			GameWindowAdapter windowAdapter, GameMenu menu) {
		initFrame(inputHandler, windowAdapter);
		initPanel(core, inputHandler, menu);
	}

	private void initPanel(Core core, InputManager handler, GameMenu menu) {
		gamePanel = new GamePanel(core, this, handler, menu);
		gameFrame.add(gamePanel);
	}

	private void initFrame(InputManager inputHandler,
			GameWindowAdapter windowAdapter) {
		gameFrame = new GameFrame(inputHandler, windowAdapter);
	}

	public void setRenderedWorld(World w) {
		this.renderedWorld = w;
	}

	public World getRenderedWorld() {
		return renderedWorld;
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
