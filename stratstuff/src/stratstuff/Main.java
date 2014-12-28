package stratstuff;

import java.util.ArrayList;

public class Main implements Runnable {

	private World world;
	private GameCamera gameCamera;
	private GameCursor gameCursor;

	private static DebugConsole debugConsole;

	private ArrayList<Updatable> updatables;

	private VisualManager visualManager;
	private WorldManager worldManager;
	private InputManager inputManager;
	private TaskManager taskManager;
	private UnitManager unitManager;

	public Main() {
		updatables = new ArrayList<Updatable>();

		debugConsole = new DebugConsole(this);
		updatables.add(debugConsole);

		Element.loadElements();
		Ground.loadGrounds();
		world = WorldLoader.loadWorld(this, "test");

		gameCamera = new GameCamera(this);
		gameCursor = new GameCursor(gameCamera);

		createUpdatables();

		world.initialCreationOfEdges();

		visualManager.activate();
	}

	private void createUpdatables() {
		inputManager = new InputManager(gameCamera, gameCursor);
		updatables.add(inputManager);

		visualManager = new VisualManager(world, gameCamera, inputManager,
				gameCursor);
		updatables.add(visualManager);

		worldManager = new WorldManager(world);
		updatables.add(worldManager);

		taskManager = new TaskManager(this);
		updatables.add(taskManager);

		unitManager = new UnitManager(this);
		updatables.add(unitManager);
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

	public static void printDebug(String message) {
		debugConsole.print(message);
	}

	public static void main(String[] args) {
		new Thread(new Main()).start();
	}

	public GameCamera getCamera() {
		return gameCamera;
	}

	public World getWorld() {
		return world;
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public UnitManager getUnitManager() {
		return unitManager;
	}
}
