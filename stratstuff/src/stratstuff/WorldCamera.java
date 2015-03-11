package stratstuff;

public class WorldCamera implements Updatable {

	private int cameraLayer;
	private int cameraXWP;
	private int cameraYWP;
	private int cameraMicroX;
	private int cameraMicroY;
	private int cameraSpeedX;
	private int cameraSpeedY;
	private int xInactiveCounter = 5;
	private int yInactiveCounter = 5;
	private World world;
	private WorldCursor cursor;

	public WorldCamera(World world) {
		this.world = world;
		cameraXWP = 0;
		cameraYWP = 0;
		cameraMicroX = 0;
		cameraMicroY = 0;
		cameraSpeedX = 0;
		cameraSpeedY = 0;
		cameraLayer = 0;
	}

	public void setCursor(WorldCursor c) {
		this.cursor = c;
	}

	public World getWorld() {
		return world;
	}

	public void goDeeper() {
		if (cameraLayer < world.getDepth() - 1) {
			cameraLayer++;
		}
	}

	public void goHigher() {
		if (cameraLayer > 0) {
			cameraLayer--;
		}
	}

	public int getMicroX() {
		return cameraMicroX;
	}

	public int getMicroY() {
		return cameraMicroY;
	}

	public int getStartX() {
		return cameraXWP;
	}

	public int getStartY() {
		return cameraYWP;
	}

	public int getEndX() {
		return cameraXWP
				+ ((GameSettings.GAME_FRAME_WIDTH - GameSettings.MENU_WIDTH) / GameSettings.TILE_SIZE);
	}

	public int getEndY() {
		return cameraYWP + GameSettings.GAME_FRAME_HEIGHT
				/ GameSettings.TILE_SIZE;
	}

	public int getLayer() {
		return cameraLayer;
	}

	public void left() {
		xInactiveCounter = 5;
		if (cameraXWP < 0) {
			return;
		}
		cameraSpeedX -= 5;
		if (cameraSpeedX < -40) {
			cameraSpeedX = -40;
		}
	}

	public void right() {
		xInactiveCounter = 5;
		if (getEndX() > world.getWidth()) {
			return;
		}
		cameraSpeedX += 5;
		if (cameraSpeedX > 40) {
			cameraSpeedX = 40;
		}
	}

	public void up() {
		yInactiveCounter = 5;
		if (cameraYWP < 0) {
			return;
		}
		cameraSpeedY -= 5;
		if (cameraSpeedY < -40) {
			cameraSpeedY = -40;
		}
	}

	public void down() {
		yInactiveCounter = 5;
		if (getEndY() > world.getHeight()) {
			return;
		}
		cameraSpeedY += 5;
		if (cameraSpeedY > 40) {
			cameraSpeedY = 40;
		}
	}

	private void moveUp() {
		cameraYWP--;
		cursor.moveUp();
	}

	private void moveDown() {
		cameraYWP++;
		cursor.moveDown();
	}

	private void moveRight() {
		cameraXWP++;
		cursor.moveRight();
	}

	private void moveLeft() {
		cameraXWP--;
		cursor.moveLeft();
	}

	@Override
	public void update() {
		xInactiveCounter--;
		yInactiveCounter--;
		if (xInactiveCounter < 0)
			xInactiveCounter = 0;
		if (yInactiveCounter < 0)
			yInactiveCounter = 0;

		if (xInactiveCounter == 0) {
			if (cameraSpeedX > 0)
				cameraSpeedX--;
			else if (cameraSpeedX < 0) {
				cameraSpeedX++;
			}
		}
		if (yInactiveCounter == 0) {
			if (cameraSpeedY > 0)
				cameraSpeedY--;
			else if (cameraSpeedY < 0) {
				cameraSpeedY++;
			}
		}

		if (cameraXWP < 0) {
			xInactiveCounter = 2;
			cameraSpeedX += 2;
		} else if (getEndX() > world.getWidth()) {
			xInactiveCounter = 2;
			cameraSpeedX -= 2;
		}
		if (cameraYWP < 0) {
			yInactiveCounter = 2;
			cameraSpeedY += 2;
		} else if (getEndY() > world.getHeight()) {
			yInactiveCounter = 2;
			cameraSpeedY -= 2;
		}

		cameraMicroX += cameraSpeedX;
		cameraMicroY += cameraSpeedY;

		int t = GameSettings.TILE_SIZE;

		while (true) {
			if (cameraMicroX > t) {
				if (cameraXWP == -1) {
					cameraSpeedX = 0;
				}
				moveRight();
				cameraMicroX -= t;
			} else if (cameraMicroX < 0) {
				if (getEndX() == world.getWidth()) {
					cameraSpeedX = 0;
				}
				moveLeft();
				cameraMicroX += t;
			} else {
				break;
			}
		}

		while (true) {
			if (cameraMicroY > t) {
				if (cameraYWP == -1) {
					cameraSpeedY = 0;
				}
				moveDown();
				cameraMicroY -= t;
			} else if (cameraMicroY < 0) {
				if (getEndY() == world.getHeight()) {
					cameraSpeedY = 0;
				}
				moveUp();
				cameraMicroY += t;
			} else {
				break;
			}
		}

	}
}
