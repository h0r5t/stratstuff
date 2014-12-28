package stratstuff;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public class InputManager implements Updatable {

	private GameCamera camera;
	private GameCursor cursor;

	private HashMap<Integer, Boolean> keyMap;

	public InputManager(GameCamera cam, GameCursor cursor) {
		this.camera = cam;
		this.cursor = cursor;
		keyMap = new HashMap<Integer, Boolean>();
		keyMap.put(KeyEvent.VK_UP, false);
		keyMap.put(KeyEvent.VK_DOWN, false);
		keyMap.put(KeyEvent.VK_RIGHT, false);
		keyMap.put(KeyEvent.VK_LEFT, false);

		keyMap.put(KeyEvent.VK_W, false);
		keyMap.put(KeyEvent.VK_S, false);
		keyMap.put(KeyEvent.VK_D, false);
		keyMap.put(KeyEvent.VK_A, false);
		keyMap.put(KeyEvent.VK_Y, false);
		keyMap.put(KeyEvent.VK_X, false);
		keyMap.put(KeyEvent.VK_SHIFT, false);

		// debug
		keyMap.put(KeyEvent.VK_ENTER, false);
	}

	@Override
	public void update() {
		if (keyMap.get(KeyEvent.VK_UP)) {
			camera.moveUp();
		}
		if (keyMap.get(KeyEvent.VK_DOWN)) {
			camera.moveDown();
		}
		if (keyMap.get(KeyEvent.VK_RIGHT)) {
			camera.moveRight();
		}
		if (keyMap.get(KeyEvent.VK_LEFT)) {
			camera.moveLeft();
		}

		if (keyMap.get(KeyEvent.VK_W)) {
			cursor.moveUp();
		}
		if (keyMap.get(KeyEvent.VK_S)) {
			cursor.moveDown();
		}
		if (keyMap.get(KeyEvent.VK_D)) {
			cursor.moveRight();
		}
		if (keyMap.get(KeyEvent.VK_A)) {
			cursor.moveLeft();
		}

		if (keyMap.get(KeyEvent.VK_X)) {
			camera.goDeeper();
		}
		if (keyMap.get(KeyEvent.VK_Y)) {
			camera.goHigher();
		}
	}

	public void keyPressed(KeyEvent e) {
		keyMap.put(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		keyMap.put(e.getKeyCode(), false);
	}
}
