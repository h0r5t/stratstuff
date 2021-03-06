package stratstuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class WorldView implements View {

	private Core core;
	private World world;
	private ArrayList<UIElement> uiElements;
	private Unit selectedUnit;

	public WorldView(Core core, World world) {
		this.core = core;
		this.world = world;
		this.uiElements = new ArrayList<UIElement>();
	}

	public World getWorld() {
		return world;
	}

	@Override
	public void draw(Graphics2D g) {
		drawWPs(g);
		drawMicroObjects(g);
		drawCursor(g);
		drawSelectionArea(g);
		drawUIElements(g);
	}

	private void drawUIElements(Graphics2D g) {
		for (UIElement e : uiElements) {
			e.draw(g, 0, 0);
		}

		if (selectedUnit == null)
			return;

		// draw selection marker
		int x = selectedUnit.getMovingObject().getPosition().getX();
		int y = selectedUnit.getMovingObject().getPosition().getY();
		WorldCamera cam = world.getGameCamera();
		int pixelX = (x - cam.getStartX()) * GameSettings.TILE_SIZE - cam.getMicroX();
		int pixelY = (y - cam.getStartY()) * GameSettings.TILE_SIZE - cam.getMicroY();
		g.setColor(Color.CYAN);
		g.drawOval(pixelX - 7, pixelY - 7, 30, 30);
	}

	private void drawSelectionArea(Graphics2D g) {
		InputManager inputHandler = core.getInputManager();
		WorldCamera cam = world.getGameCamera();

		if (inputHandler.selectionAreaIsOn()) {
			g.setColor(new Color(0f, 1f, 0f, .5f));

			Area3D r = inputHandler.getSelectionArea();

			g.fillRect((r.x - cam.getStartX()) * GameSettings.TILE_SIZE,
					(r.y - cam.getStartY()) * GameSettings.TILE_SIZE, r.w * GameSettings.TILE_SIZE,
					r.h * GameSettings.TILE_SIZE);
		}
	}

	private void drawWPs(Graphics2D g) {
		WorldCamera cam = world.getGameCamera();

		if (cam == null)
			return;
		int layer = cam.getLayer();
		for (int x = cam.getStartX(); x < cam.getEndX(); x++) {
			for (int y = cam.getStartY(); y < cam.getEndY(); y++) {
				if (x < 0 || y < 0 || x > world.getWidth() - 1 || y > world.getHeight() - 1) {
					g.setColor(Color.BLACK);
					g.fillRect((x - cam.getStartX()) * GameSettings.TILE_SIZE - cam.getMicroX(),
							(y - cam.getStartY()) * GameSettings.TILE_SIZE - cam.getMicroY(), GameSettings.TILE_SIZE,
							GameSettings.TILE_SIZE);
				} else {
					world.getWP(x, y, layer).draw(g, (x - cam.getStartX()) * GameSettings.TILE_SIZE - cam.getMicroX(),
							(y - cam.getStartY()) * GameSettings.TILE_SIZE - cam.getMicroY());

					Image image = world.getShadowImage(world.getWP(x, y, layer));
					g.drawImage(image, (x - cam.getStartX()) * GameSettings.TILE_SIZE - cam.getMicroX(),
							(y - cam.getStartY()) * GameSettings.TILE_SIZE - cam.getMicroY(), GameSettings.TILE_SIZE,
							GameSettings.TILE_SIZE, null);
				}

			}
		}
	}

	private void drawMicroObjects(Graphics2D g2) {
		WorldCamera cam = world.getGameCamera();

		if (cam == null)
			return;
		int layer = cam.getLayer();
		for (int x = cam.getStartX(); x < cam.getEndX(); x++) {
			for (int y = cam.getStartY(); y < cam.getEndY(); y++) {
				if (x < 0 || y < 0 || x > world.getWidth() - 1 || y > world.getHeight() - 1)
					return;
				world.getWP(x, y, layer).drawMicroObjects(g2,
						(x - cam.getStartX()) * GameSettings.TILE_SIZE - cam.getMicroX(),
						(y - cam.getStartY()) * GameSettings.TILE_SIZE - cam.getMicroY());
			}
		}
	}

	private void drawCursor(Graphics2D g) {
		WorldCursor cursor = world.getGameCursor();
		WorldCamera cam = world.getGameCamera();

		if (cursor == null)
			return;
		cursor.draw(g, (cursor.getX() - cam.getStartX()) * GameSettings.TILE_SIZE - cam.getMicroX(),
				(cursor.getY() - cam.getStartY()) * GameSettings.TILE_SIZE - cam.getMicroY());
	}

	private void removeAllTooltips() {
		ArrayList<UIElement> toRemove = new ArrayList<UIElement>();
		for (UIElement uie : uiElements) {
			if (uie.getType() == UIElementType.Tooltip)
				toRemove.add(uie);
		}
		uiElements.removeAll(toRemove);
	}

	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_X) {
			world.getGameCamera().goDeeper();
		}

		else if (e.getKeyCode() == KeyEvent.VK_Y) {
			world.getGameCamera().goHigher();
		}
	}

	@Override
	public void keysArePressed(DefaultHashMap<Integer, Boolean> keyMap) {
		if (keyMap.getDefault(KeyEvent.VK_W, false)) {
			world.getGameCamera().up();
		}
		if (keyMap.getDefault(KeyEvent.VK_S, false)) {
			world.getGameCamera().down();
		}
		if (keyMap.getDefault(KeyEvent.VK_D, false)) {
			world.getGameCamera().right();
		}
		if (keyMap.getDefault(KeyEvent.VK_A, false)) {
			world.getGameCamera().left();
		}

		if (keyMap.getDefault(KeyEvent.VK_UP, false)) {
			world.getGameCursor().moveUp();
		}
		if (keyMap.getDefault(KeyEvent.VK_DOWN, false)) {
			world.getGameCursor().moveDown();
		}
		if (keyMap.getDefault(KeyEvent.VK_RIGHT, false)) {
			world.getGameCursor().moveRight();
		}
		if (keyMap.getDefault(KeyEvent.VK_LEFT, false)) {
			world.getGameCursor().moveLeft();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for (UIElement element : uiElements) {
			if (element.overridesInput(e.getX(), e.getY())) {
				element.mouseClicked(e);
				removeAllTooltips();
				return;
			}
		}

		if (e.getButton() == MouseEvent.BUTTON1) {
			removeAllTooltips();
			checkForUnitSelection();

		} else if (e.getButton() == MouseEvent.BUTTON3) {
			removeAllTooltips();
			if (selectedUnit == null)
				return;
			WorldCamera cam = world.getGameCamera();
			int mouseX = e.getX();
			int mouseY = e.getY();

			int cameraX = core.getCamera().getStartX() * GameSettings.TILE_SIZE;
			int cameraY = core.getCamera().getStartY() * GameSettings.TILE_SIZE;

			int tileX = (mouseX + cameraX + core.getCamera().getMicroX()) / GameSettings.TILE_SIZE;
			int tileY = (mouseY + cameraY + core.getCamera().getMicroY()) / GameSettings.TILE_SIZE;

			WorldPoint wp = world.getWP(tileX, tileY, cam.getLayer());
			int menuX = e.getX();
			int menuY = e.getY() + GameSettings.TILE_SIZE;
			ContextCommandsMenu menu = new ContextCommandsMenu(selectedUnit.getContextCommands(), menuX, menuY, wp);
			uiElements.add(menu);
		}
	}

	private void checkForUnitSelection() {
		WorldCursor cursor = world.getGameCursor();
		WorldCamera cam = world.getGameCamera();
		int tileX = cursor.getX();
		int tileY = cursor.getY();

		ArrayList<MovingObject> objectsOnWP = world.getWP(tileX, tileY, cam.getLayer()).getAttachedMovingObjects();
		for (MovingObject obj : objectsOnWP) {
			Unit unit = world.getUnitByObjectID(obj.getUniqueID());
			if (unit != null) {
				selectedUnit = unit;
				break;
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (UIElement element : uiElements) {
			if (element.overridesInput(e.getX(), e.getY())) {
				element.mouseMoved(e);
				return;
			}
		}

		int mouseX = e.getX();
		int mouseY = e.getY();

		int cameraX = core.getCamera().getStartX() * GameSettings.TILE_SIZE;
		int cameraY = core.getCamera().getStartY() * GameSettings.TILE_SIZE;

		int tileX = (mouseX + cameraX + core.getCamera().getMicroX()) / GameSettings.TILE_SIZE;
		int tileY = (mouseY + cameraY + core.getCamera().getMicroY()) / GameSettings.TILE_SIZE;

		core.getCursor().setXY(tileX, tileY);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

}
