package stratstuff;

public class GameCamera implements Updatable {

	private int cameraLayer;
	private int cameraXWP;
	private int cameraYWP;
	private int cameraMicroX;
	private int cameraMicroY;
	private int cameraSpeedX;
	private int cameraSpeedY;
	private int xInactiveCounter = 5;
	private int yInactiveCounter = 5;
	private Core main;

	public GameCamera(Core main) {
		this.main = main;
		cameraXWP = 0;
		cameraYWP = 0;
		cameraMicroX = 0;
		cameraMicroY = 0;
		cameraSpeedX = 0;
		cameraSpeedY = 0;
		cameraLayer = 0;
	}

	public void goDeeper() {
		if (cameraLayer < GameSettings.WORLD_DEPTH - 1) {
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
		if (getEndX() > GameSettings.WORLD_WIDTH) {
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
		if (getEndY() > GameSettings.WORLD_HEIGHT) {
			return;
		}
		cameraSpeedY += 5;
		if (cameraSpeedY > 40) {
			cameraSpeedY = 40;
		}
	}

	private void moveUp() {
		cameraYWP--;
		main.getCursor().moveUp();
	}

	private void moveDown() {
		cameraYWP++;
		main.getCursor().moveDown();
	}

	private void moveRight() {
		cameraXWP++;
		main.getCursor().moveRight();
	}

	private void moveLeft() {
		cameraXWP--;
		main.getCursor().moveLeft();
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
		} else if (getEndX() > GameSettings.WORLD_WIDTH) {
			xInactiveCounter = 2;
			cameraSpeedX -= 2;
		}
		if (cameraYWP < 0) {
			yInactiveCounter = 2;
			cameraSpeedY += 2;
		} else if (getEndY() > GameSettings.WORLD_HEIGHT) {
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
				if (getEndX() == GameSettings.WORLD_WIDTH) {
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
				if (getEndY() == GameSettings.WORLD_HEIGHT) {
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
