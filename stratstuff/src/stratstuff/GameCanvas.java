package stratstuff;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameCanvas extends Canvas {

	private Image bufImage;
	private Graphics bufG;

	private Core core;
	private World world;
	private GameCamera cam;
	private GameCursor cursor;
	private InputManager inputHandler;
	private VisualManager visualManager;
	private GameMenu gameMenu;
	private InfoScreen currentInfoScreen;

	public GameCanvas(Core core, World world, VisualManager visualManager,
			InputManager handler, GameCamera cam, GameCursor cursor,
			GameMenu gameMenu) {
		this.core = core;
		this.world = world;
		this.visualManager = visualManager;
		this.inputHandler = handler;
		this.gameMenu = gameMenu;
		this.cam = cam;
		this.cursor = cursor;
		addKeyListener(new AL());
	}

	@Override
	public void paint(Graphics g) {
		if (!visualManager.drawNow()) {
			return;
		}
		Graphics2D g2 = (Graphics2D) g;

		drawWPs(g2);
		drawGameMenu(g2);
		drawCursor(g2);
		drawSelectionArea(g2);

		drawInfoScreen(g2);
	}

	private void drawInfoScreen(Graphics2D g2) {
		if (currentInfoScreen != null) {
			currentInfoScreen.draw(g2, 100, 100);
		}
	}

	public void setInfoScreen(InfoScreen screen) {
		currentInfoScreen = screen;
	}

	public void leaveInfoScreen() {
		currentInfoScreen = null;
	}

	private void drawSelectionArea(Graphics2D g) {
		if (inputHandler.selectionAreaIsOn()) {
			g.setColor(new Color(0f, 1f, 0f, .5f));

			Area3D r = inputHandler.getSelectionArea();

			g.fillRect((r.x - cam.getStartX()) * GameSettings.TILE_SIZE,
					(r.y - cam.getStartY()) * GameSettings.TILE_SIZE, r.w
							* GameSettings.TILE_SIZE, r.h
							* GameSettings.TILE_SIZE);
		}
	}

	private void drawGameMenu(Graphics2D g2) {
		gameMenu.draw(g2, -1, -1);
	}

	public boolean infoScreenIsShown() {
		return currentInfoScreen != null;
	}

	private void drawWPs(Graphics2D g) {
		int layer = cam.getLayer();
		for (int x = cam.getStartX(); x < cam.getEndX(); x++) {
			for (int y = cam.getStartY(); y < cam.getEndY(); y++) {
				world.getWP(x, y, layer).draw(g,
						(x - cam.getStartX()) * GameSettings.TILE_SIZE,
						(y - cam.getStartY()) * GameSettings.TILE_SIZE);

				Image image = core.getLightManager().getShadowImage(
						world.getWP(x, y, layer));
				g.drawImage(image, (x - cam.getStartX())
						* GameSettings.TILE_SIZE, (y - cam.getStartY())
						* GameSettings.TILE_SIZE, GameSettings.TILE_SIZE,
						GameSettings.TILE_SIZE, null);
			}
		}
	}

	private void drawCursor(Graphics2D g) {
		cursor.draw(g, (cursor.getX() - cam.getStartX())
				* GameSettings.TILE_SIZE, (cursor.getY() - cam.getStartY())
				* GameSettings.TILE_SIZE);
	}

	@Override
	public void update(Graphics g) {
		int w = this.getSize().width;
		int h = this.getSize().height;

		if (bufImage == null) {
			bufImage = this.createImage(w, h);
			bufG = bufImage.getGraphics();
		}

		bufG.setColor(this.getBackground());
		bufG.fillRect(0, 0, w, h);

		bufG.setColor(this.getForeground());

		paint(bufG);

		g.drawImage(bufImage, 0, 0, this);
	}

	private class AL implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			inputHandler.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			inputHandler.keyReleased(e);
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// needed?
		}
	}
}
