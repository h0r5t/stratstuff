package stratstuff;

import java.util.ArrayList;

public class Core implements Runnable {

	private World world;
	private GameCamera gameCamera;
	private GameCursor gameCursor;
	private GameMenu gameMenu;

	private static DebugConsole debugConsole;

	private ArrayList<Updatable> updatables;

	private VisualManager visualManager;
	private WorldManager worldManager;
	private InputManager inputManager;
	private TaskManager taskManager;
	private ObjectManager objectManager;
	private LightManager lightManager;

	private GameWindowAdapter windowAdapter;

	private SimpleTimer timer;

	private boolean locked = false;

	private static FrontendAdapter frontendAdapter;

	public Core() {
		updatables = new ArrayList<Updatable>();

		windowAdapter = new GameWindowAdapter(this);

		Element.loadElements();
		Ground.loadGrounds();
		MovingObject.loadFromInfoFile();

		gameMenu = new GameMenu(this);

		debugConsole = new DebugConsole(this, windowAdapter);
		updatables.add(debugConsole);

		gameCamera = new GameCamera(this);
		gameCursor = new GameCursor(gameCamera);
		timer = new SimpleTimer();

		createUpdatables();

		loadOrGenerateWorld();

		visualManager = new VisualManager(this, world, gameCamera,
				inputManager, gameCursor, windowAdapter, gameMenu);
		updatables.add(visualManager);

		world.initialCreationOfEdges();

		lightManager = new LightManager(this);
		updatables.add(lightManager);

		frontendAdapter.startPythonFrontend();

		visualManager.activate();

		debugConsole.runDefaultScript();
	}

	private void loadOrGenerateWorld() {
		if (GameSettings.GENERATE_NEW_WORLD) {
			world = WorldGenerator.generateWorld(this);
			PersistanceManager.save(this, "test");
		} else {
			world = PersistanceManager.load(this, "test");
		}
	}

	private void createUpdatables() {
		frontendAdapter = new FrontendAdapter(this);
		frontendAdapter.start();

		inputManager = new InputManager(gameCamera, gameCursor, gameMenu);
		updatables.add(inputManager);

		worldManager = new WorldManager(world);
		updatables.add(worldManager);

		taskManager = new TaskManager(this);
		updatables.add(taskManager);

		objectManager = new ObjectManager(this);
		updatables.add(objectManager);
	}

	public GameCursor getCursor() {
		return gameCursor;
	}

	@Override
	public void run() {
		try {
			update();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void update() throws InterruptedException {

		initialSleep();
		timer.start();
		while (true) {
			if (!locked) {
				long sleepTime = GameSettings.TICK_MILLIS - timer.stop();
				if (sleepTime > 0)
					Thread.sleep(sleepTime);

				updateUpdatables();

				frontendAdapter.sendStartMessage();
				timer.start();

				lock();
			}

			frontendAdapter.waitForFrontendFIN();

			Thread.sleep(5);
		}

	}

	private void lock() {
		locked = true;
	}

	private void initialSleep() {
		try {
			Thread.sleep(GameSettings.INITIAL_SLEEP_TIME);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void unlock() {
		locked = false;
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

	public GameCanvas getCanvas() {
		return visualManager.getCanvas();
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public ObjectManager getObjectManager() {
		return objectManager;
	}

	public FrontendAdapter getFrontendAdapter() {
		return frontendAdapter;
	}

	public DebugConsole getConsole() {
		return debugConsole;
	}

	public LightManager getLightManager() {
		return lightManager;
	}

	public static void tellFrontend(String message) {
		frontendAdapter.addToQueue(message);
	}

	public void saveWorldState() {
		PersistanceManager.save(this, "test");
	}

	public InputManager getInputManager() {
		return inputManager;
	}

	public void tellFrontendToShutdown() {
		frontendAdapter.sendShutDownMessage();
	}
}
