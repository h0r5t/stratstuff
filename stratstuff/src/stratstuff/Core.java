package stratstuff;

import java.util.ArrayList;

public class Core implements Runnable {

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

	private GameWindowAdapter windowAdapter;

	private static FrontendAdapter frontendAdapter;

	public Core() {
		updatables = new ArrayList<Updatable>();

		windowAdapter = new GameWindowAdapter(this);

		debugConsole = new DebugConsole(this, windowAdapter);
		updatables.add(debugConsole);

		Element.loadElements();
		Ground.loadGrounds();

		gameCamera = new GameCamera(this);
		gameCursor = new GameCursor(gameCamera);

		createUpdatables();

		loadOrGenerateWorld();

		visualManager = new VisualManager(world, gameCamera, inputManager,
				gameCursor, windowAdapter);
		updatables.add(visualManager);

		world.initialCreationOfEdges();

		frontendAdapter.startPythonFrontend();

		visualManager.activate();

		debugConsole.runDefaultScript();
	}

	private void loadOrGenerateWorld() {
		if (GameSettings.GENERATE_NEW_WORLD) {
			world = WorldGenerator.generateWorld(this);
		} else {
			world = PersistanceManager.load(this, "test");
		}
	}

	private void createUpdatables() {
		frontendAdapter = new FrontendAdapter(this);
		frontendAdapter.start();
		updatables.add(frontendAdapter);

		inputManager = new InputManager(gameCamera, gameCursor);
		updatables.add(inputManager);

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
		try {
			while (true) {

				updateUpdatables();

				Thread.sleep(50);

				frontendAdapter.update();

				Thread.sleep(50);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
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
		new Thread(new Core()).start();
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

	public FrontendAdapter getFrontendAdapter() {
		return frontendAdapter;
	}

	public DebugConsole getConsole() {
		return debugConsole;
	}

	public static void tellFrontend(String message) {
		frontendAdapter.addToQueue(message);
	}

	public void saveWorldState() {
		PersistanceManager.save(this, "test");
	}
}
