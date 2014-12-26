package stratstuff;

import java.util.ArrayList;

public class Main implements Runnable {

	private World world;
	private GameCamera gameCamera;
	private GameCursor gameCursor;

	private ArrayList<Updatable> updatables;

	private VisualManager visualManager;
	private WorldManager worldManager;
	private InputManager inputManager;

	public Main() {
		Element.loadElements();
		Ground.loadGrounds();
		world = WorldLoader.loadWorld("test");

		gameCamera = new GameCamera(this);
		gameCursor = new GameCursor(gameCamera);

		createUpdatables();

		worldManager.initialCreationOfEdges();

		visualManager.activate();
	}

	private void createUpdatables() {
		updatables = new ArrayList<Updatable>();

		inputManager = new InputManager(world, gameCamera, gameCursor);
		updatables.add(inputManager);

		visualManager = new VisualManager(world, gameCamera, inputManager,
				gameCursor);
		updatables.add(visualManager);

		worldManager = new WorldManager(world);
		updatables.add(worldManager);
	}

	public GameCursor getCursor() {
		return gameCursor;
	}

	@Override
	public void run() {
		while (true) {

			updateUpdatables();

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void updateUpdatables() {
		for (Updatable u : updatables) {
			u.update();
		}
	}

	public static void main(String[] args) {
		new Thread(new Main()).start();
	}
}
