package stratstuff;

public class WorldSimulator implements Updatable {

	private World world;
	private LightManager lightManager;
	private GameCamera camera;
	private GameCursor cursor;

	public WorldSimulator(Core core, World w) {
		this.world = w;
		lightManager = world.getLightManager();
		camera = new GameCamera(w);
		cursor = new GameCursor(camera);
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

	public GameCamera getGameCamera() {
		return camera;
	}

	public GameCursor getGameCursor() {
		return cursor;
	}

}
