package stratstuff;

public class WorldSimulator implements Updatable {

	private World world;
	private LightManager lightManager;
	private WorldCamera camera;
	private WorldCursor cursor;

	public WorldSimulator(Core core, World w) {
		this.world = w;
		lightManager = world.getLightManager();
		camera = new WorldCamera(w);
		cursor = new WorldCursor(camera);
		world.setGameCamera(camera);
		world.setGameCursor(cursor);
	}

	@Override
	public void update() {
		world.update();
		lightManager.update();
		camera.update();
	}

	public World getWorld() {
		return world;
	}

	public LightManager getLightManager() {
		return lightManager;
	}

	public WorldCamera getGameCamera() {
		return camera;
	}

	public WorldCursor getGameCursor() {
		return cursor;
	}

}
