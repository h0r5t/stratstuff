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
	private UnitManager unitManager;
	private ItemManager itemManager;

	private GameWindowAdapter windowAdapter;
	private DesignFrame developerFrame;

	private SimpleTimer timer;

	private boolean locked = false;
	private boolean gameIsPaused = false;

	private static FrontendAdapter frontendAdapter;

	public Core() {
		updatables = new ArrayList<Updatable>();

		windowAdapter = new GameWindowAdapter(this);

		Element.loadElements();
		Ground.loadGrounds();
		MovingObject.loadFromInfoFile();
		Item.loadFromInfoFile();
		Unit.loadFromInfoFile();

		gameMenu = new GameMenu(this);

		debugConsole = new DebugConsole(this, windowAdapter);
		updatables.add(debugConsole);

		gameCamera = new GameCamera(this);
		updatables.add(gameCamera);
		gameCursor = new GameCursor(gameCamera);
		timer = new SimpleTimer();

		lightManager = new LightManager(this);
		updatables.add(lightManager);

		createUpdatables();

		loadOrGenerateWorld();

		visualManager = new VisualManager(this, world, gameCamera,
				inputManager, gameCursor, windowAdapter, gameMenu);
		updatables.add(visualManager);

		lightManager.initLights();

		world.initialCreationOfEdges();

		frontendAdapter.startPythonFrontend();

		developerFrame.makeUnitComboBox();

		visualManager.activate();
	}

	private void loadOrGenerateWorld() {
		if (GameSettings.GENERATE_NEW_WORLD) {
			if (GameSettings.KEEP_STUFF)
				world = PersistanceManager.load(this, "test");
			else
				world = new World(this);
			world = WorldGenerator.generateWorld(this, world);
			PersistanceManager.save(this, "test");
		} else {
			world = PersistanceManager.load(this, "test");
		}
	}

	private void createUpdatables() {
		frontendAdapter = new FrontendAdapter(this);
		frontendAdapter.start();

		developerFrame = new DesignFrame(this);
		updatables.add(developerFrame);

		worldManager = new WorldManager(world);
		updatables.add(worldManager);

		taskManager = new TaskManager(this);
		updatables.add(taskManager);

		objectManager = new ObjectManager(this);
		updatables.add(objectManager);

		unitManager = new UnitManager(this);
		updatables.add(unitManager);

		itemManager = new ItemManager(this);
		updatables.add(itemManager);

		inputManager = new InputManager(this, gameCamera, gameCursor, gameMenu);
		updatables.add(inputManager);
	}

	public GameCursor getCursor() {
		return gameCursor;
	}

	public void togglePause() {
		gameIsPaused = !gameIsPaused;
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

		unitManager.registerRobots();

		while (true) {
			if (!locked) {
				long sleepTime = GameSettings.TICK_MILLIS - timer.stop();
				if (sleepTime > 0)
					Thread.sleep(sleepTime);

				if (!gameIsPaused) {
					updateUpdatables();

					frontendAdapter.sendStartMessage();
					timer.start();

					lock();
				} else {
					visualManager.update();
					inputManager.update();
					gameCamera.update();
					Thread.sleep(GameSettings.TICK_MILLIS);
				}
			}

			frontendAdapter.waitForFrontendFIN();

			Thread.sleep(5);

		}

	}

	public boolean gameIsPaused() {
		return gameIsPaused;
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

	public GamePanel getCanvas() {
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

	public void save() {
		PersistanceManager.save(this, "test");
		developerFrame.save();
	}

	public InputManager getInputManager() {
		return inputManager;
	}

	public DesignFrame getDeveloperFrame() {
		return developerFrame;
	}

	public void tellFrontendToShutdown() {
		frontendAdapter.sendShutDownMessage();
	}

	public UnitManager getUnitManager() {
		return unitManager;
	}

	public ItemManager getItemManager() {
		return itemManager;
	}
}
