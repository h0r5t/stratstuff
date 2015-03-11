package stratstuff;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class InputManager implements Updatable {

	private Core core;
	private WorldCamera camera;
	private WorldCursor cursor;
	private GameMenu menu;
	private boolean selectionAreaIsOn = false;
	private Area3D selectionArea;

	private DefaultHashMap<Integer, Boolean> keyMap;

	public InputManager(Core core, GameMenu menu) {
		this.core = core;
		this.menu = menu;

		keyMap = new DefaultHashMap<Integer, Boolean>();
	}

	@Override
	public void update() {
		core.getVisualManager().getRenderedView().keysArePressed(keyMap);
	}

	public void keyPressed(KeyEvent e) {
		keyMap.put(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		keyMap.put(e.getKeyCode(), false);
		menu.evaluateInput(e.getKeyCode());

		if (e.getKeyCode() == KeyEvent.VK_F12) {
			core.getDeveloperFrame().show();
			if (!core.gameIsPaused())
				core.togglePause();
		}

		else if (e.getKeyCode() == KeyEvent.VK_0) {
			core.test0();
		}

		else if (e.getKeyCode() == KeyEvent.VK_1) {
			core.test1();
		}

		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			core.test2();
		}

		else if (e.getKeyCode() == KeyEvent.VK_P) {
			core.togglePause();
		}

		else {
			core.getVisualManager().getRenderedView().keyReleased(e);
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
	}

	public Area3D getSelectionArea() {
		return selectionArea;
	}

	public void mouseClicked(MouseEvent e) {
		core.getVisualManager().getRenderedView().mouseClicked(e);
	}

	public void mouseMoved(MouseEvent e) {
		core.getVisualManager().getRenderedView().mouseMoved(e);
	}
}
