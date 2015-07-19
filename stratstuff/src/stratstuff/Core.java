package stratstuff;

import java.io.File;
import java.util.ArrayList;

public class Core implements Runnable {

	private GameMenu gameMenu;

	private ArrayList<Updatable> updatables;

	private VisualManager visualManager;
	private SimulationManager simulationManager;
	private InputManager inputManager;
	private TaskManager taskManager;
	private ObjectManager objectManager;
	private UnitManager unitManager;
	private ItemManager itemManager;

	private GameWindowAdapter windowAdapter;
	private DevFrame developerFrame;

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

		timer = new SimpleTimer();

		createUpdatables();

		loadGalaxies();
		loadWorlds();

		visualManager = new VisualManager(this, inputManager, windowAdapter,
				gameMenu);
		updatables.add(visualManager);

		simulationManager.initLights();
		simulationManager.initialCreationOfEdges();

		frontendAdapter.startPythonFrontend();

		developerFrame.makeUnitComboBox();

		visualManager.setRenderedView(new WorldView(this, simulationManager
				.getWorldWithName("world1")));

		visualManager.activate();
	}

	private void loadGalaxies() {
		// Galaxy galaxy;
		// File galaxiesDir = new File(FileSystem.GALAXIES_DIR);
		//
		// for (File f : galaxiesDir.listFiles()) {
		// if (f.getName().startsWith("galaxy")) {
		// galaxy = PersistanceManager.loadGalaxy(this, f.getName());
		// simulationManager.addSimulator(new GalaxySimulator(galaxy));
		// }
		// }

		Galaxy galaxy = GalaxyGenerator.generateGalaxy("galaxy0");
		simulationManager.addSimulator(new GalaxySimulator(galaxy));
	}

	private void loadWorlds() {
		World world;
		File worldsDir = new File(FileSystem.WORLDS_DIR);

		for (File f : worldsDir.listFiles()) {
			if (f.getName().startsWith("world")) {
				world = PersistanceManager.loadWorld(this, f.getName());
				simulationManager.addSimulator(new WorldSimulator(this, world));
			}
		}
	}

	private void createUpdatables() {
		frontendAdapter = new FrontendAdapter(this);
		frontendAdapter.start();

		developerFrame = new DevFrame(this);
		updatables.add(developerFrame);

		simulationManager = new SimulationManager();
		updatables.add(simulationManager);

		taskManager = new TaskManager(this);
		updatables.add(taskManager);

		objectManager = new ObjectManager(this);
		updatables.add(objectManager);

		unitManager = new UnitManager(this);
		updatables.add(unitManager);

		itemManager = new ItemManager(this);
		updatables.add(itemManager);

		inputManager = new InputManager(this, gameMenu);
		updatables.add(inputManager);
	}

	public WorldCursor getCursor() {
		if (!(visualManager.getRenderedView() instanceof WorldView))
			return null;
		return visualManager.getRenderedWorld().getGameCursor();
	}

	public void togglePause() {
		gameIsPaused = !gameIsPaused;
	}

	@Override
	public void run() {
		try {
			routine();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void routine() throws InterruptedException {

		initialSleep();
		timer.start();

		unitManager.registerRobots();
		unitManager.initItemsInFrontend();

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

	public static void main(String[] args) {
		new Thread(new Core()).start();
	}

	public WorldCamera getCamera() {
		return visualManager.getRenderedWorld().getGameCamera();
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

	public static void tellFrontend(String message) {
		frontendAdapter.addToQueue(message);
	}

	public void save() {
		simulationManager.saveWorlds(this);
		simulationManager.saveGalaxies();
		developerFrame.save();
	}

	public InputManager getInputManager() {
		return inputManager;
	}

	public VisualManager getVisualManager() {
		return visualManager;
	}

	public DevFrame getDeveloperFrame() {
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

	public void test0() {
		visualManager.setRenderedView(new WorldView(this, simulationManager
				.getWorldWithName("world0")));
	}

	public void test1() {
		visualManager.setRenderedView(new WorldView(this, simulationManager
				.getWorldWithName("world1")));
	}

	public void test2() {
		visualManager.setRenderedView(new GalaxyView(this, simulationManager
				.getGalaxyWithName("galaxy0")));
	}
}
