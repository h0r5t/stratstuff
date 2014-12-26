package stratstuff;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameCanvas extends Canvas {

	private Image bufImage;
	private Graphics bufG;

	private World world;
	private GameCamera cam;
	private GameCursor cursor;
	private InputManager inputHandler;

	public GameCanvas(World world, InputManager handler, GameCamera cam,
			GameCursor cursor) {
		this.world = world;
		this.inputHandler = handler;
		this.cam = cam;
		this.cursor = cursor;
		addKeyListener(new AL());
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		drawWPs(g2);
		drawCursor(g2);
	}

	private void drawWPs(Graphics2D g) {
		int layer = cam.getLayer();
		for (int x = cam.getStartX(); x < cam.getEndX(); x++) {
			for (int y = cam.getStartY(); y < cam.getEndY(); y++) {
				world.getWP(x, y, layer).draw(g,
						(x - cam.getStartX()) * GameSettings.TILE_SIZE,
						(y - cam.getStartY()) * GameSettings.TILE_SIZE);
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
			// TODO Auto-generated method stub

		}

	}

}
