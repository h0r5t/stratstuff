package stratstuff;

public class GameCamera {

	private int cameraLayer;
	private int cameraX;
	private int cameraY;
	private Main main;

	public GameCamera(Main main) {
		this.main = main;
		cameraX = 0;
		cameraY = 0;
		cameraLayer = 0;
	}

	public int getStartX() {
		return cameraX;
	}

	public int getStartY() {
		return cameraY;
	}

	public int getEndX() {
		return cameraX + GameSettings.FRAME_WIDTH / GameSettings.TILE_SIZE;
	}

	public int getEndY() {
		return cameraY + GameSettings.FRAME_HEIGHT / GameSettings.TILE_SIZE;
	}

	public int getLayer() {
		return cameraLayer;
	}

	public void moveUp() {
		if (cameraY > 0) {
			cameraY--;
			main.getCursor().moveUp();
		}
	}

	public void moveDown() {
		if (getEndY() < GameSettings.WORLD_HEIGHT) {
			cameraY++;
			main.getCursor().moveDown();
		}
	}

	public void moveRight() {
		if (getEndX() < GameSettings.WORLD_WIDTH) {
			cameraX++;
			main.getCursor().moveRight();
		}
	}

	public void moveLeft() {
		if (cameraX > 0) {
			cameraX--;
			main.getCursor().moveLeft();
		}
	}
}