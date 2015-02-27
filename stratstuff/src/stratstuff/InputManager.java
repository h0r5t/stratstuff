package stratstuff;

import java.awt.event.KeyEvent;

public class InputManager implements Updatable {

	private Core core;
	private GameCamera camera;
	private GameCursor cursor;
	private GameMenu menu;
	private boolean selectionAreaIsOn = false;
	private int selectionAreaState = 0;
	private Area3D selectionArea;

	private DefaultHashMap<Integer, Boolean> keyMap;

	public InputManager(Core core, GameCamera cam, GameCursor cursor,
			GameMenu menu) {
		this.core = core;
		this.camera = cam;
		this.cursor = cursor;
		this.menu = menu;

		keyMap = new DefaultHashMap<Integer, Boolean>();
	}

	@Override
	public void update() {
		if (keyMap.getDefault(KeyEvent.VK_UP, false)) {
			camera.moveUp();
		}
		if (keyMap.getDefault(KeyEvent.VK_DOWN, false)) {
			camera.moveDown();
		}
		if (keyMap.getDefault(KeyEvent.VK_RIGHT, false)) {
			camera.moveRight();
		}
		if (keyMap.getDefault(KeyEvent.VK_LEFT, false)) {
			camera.moveLeft();
		}

		if (!selectionAreaIsOn) {
			if (keyMap.getDefault(KeyEvent.VK_W, false)) {
				cursor.moveUp();
			}
			if (keyMap.getDefault(KeyEvent.VK_S, false)) {
				cursor.moveDown();
			}
			if (keyMap.getDefault(KeyEvent.VK_D, false)) {
				cursor.moveRight();
			}
			if (keyMap.getDefault(KeyEvent.VK_A, false)) {
				cursor.moveLeft();
			}
			if (keyMap.getDefault(KeyEvent.VK_X, false)) {
				camera.goDeeper();
			}
			if (keyMap.getDefault(KeyEvent.VK_Y, false)) {
				camera.goHigher();
			}
		}

		if (selectionAreaIsOn) {
			selectionArea.x = cursor.getX();
			selectionArea.y = cursor.getY();
			if (selectionAreaState == 0) {
				if (keyMap.getDefault(KeyEvent.VK_W, false)) {
					cursor.moveUp();
				}
				if (keyMap.getDefault(KeyEvent.VK_S, false)) {
					cursor.moveDown();
				}
				if (keyMap.getDefault(KeyEvent.VK_D, false)) {
					cursor.moveRight();
				}
				if (keyMap.getDefault(KeyEvent.VK_A, false)) {
					cursor.moveLeft();
				}
				if (keyMap.getDefault(KeyEvent.VK_X, false)) {
					camera.goDeeper();
				}
				if (keyMap.getDefault(KeyEvent.VK_Y, false)) {
					camera.goHigher();
				}
			}
			if (selectionAreaState == 1) {
				if (keyMap.getDefault(KeyEvent.VK_W, false)) {
					selectionArea.h = selectionArea.h - 1;
				}
				if (keyMap.getDefault(KeyEvent.VK_S, false)) {
					selectionArea.h = selectionArea.h + 1;
				}
				if (keyMap.getDefault(KeyEvent.VK_D, false)) {
					selectionArea.w = selectionArea.w + 1;
				}
				if (keyMap.getDefault(KeyEvent.VK_A, false)) {
					selectionArea.w = selectionArea.w - 1;
				}
				if (keyMap.getDefault(KeyEvent.VK_X, false)) {
					if (selectionArea.d < GameSettings.WORLD_DEPTH - 1)
						selectionArea.d = selectionArea.d + 1;
					camera.goDeeper();
				}
				if (keyMap.getDefault(KeyEvent.VK_Y, false)) {
					if (selectionArea.d > 0)
						selectionArea.d = selectionArea.d - 1;
					camera.goHigher();
				}
			}

		}
	}

	public void keyPressed(KeyEvent e) {
		keyMap.put(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		keyMap.put(e.getKeyCode(), false);
		menu.evaluateInput(e.getKeyCode());

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (selectionAreaIsOn) {
				if (selectionAreaState == 0) {
					selectionAreaState = 1;
					selectionArea.x = cursor.getX();
					selectionArea.y = cursor.getY();
					selectionArea.z = camera.getLayer();
				} else if (selectionAreaState == 1) {
					selectionAreaIsOn = false;
					selectionArea.d = Math.abs(camera.getLayer()
							- selectionArea.z) + 1;
					menu.areaWasSelected(selectionArea);
					selectionAreaState = 0;
				}
			}
		}

		else if (e.getKeyCode() == KeyEvent.VK_F12) {
			core.getDeveloperFrame().show();
			if (!core.gameIsPaused())
				core.togglePause();
		}

		else if (e.getKeyCode() == KeyEvent.VK_P) {
			core.togglePause();
		}
	}

	public void startSelectionArea() {
		selectionAreaIsOn = true;
		selectionArea = new Area3D(cursor.getX(), cursor.getY(),
				camera.getLayer(), 1, 1, 1);
	}

	public boolean selectionAreaIsOn() {
		return selectionAreaIsOn;
	}

	public void endSelectionArea() {
		selectionAreaIsOn = false;
		selectionAreaState = 0;
	}

	public Area3D getSelectionArea() {
		return selectionArea;
	}
}
