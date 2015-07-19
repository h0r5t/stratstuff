package stratstuff;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ConcurrentModificationException;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

	private Image bufImage;
	private Graphics bufG;

	private Core core;
	private InputManager inputHandler;
	private VisualManager visualManager;
	private GameMenu gameMenu;
	private InfoScreen currentInfoScreen;

	public GamePanel(Core core, VisualManager visualManager,
			InputManager handler, GameMenu gameMenu) {
		this.core = core;
		this.visualManager = visualManager;
		this.inputHandler = handler;
		this.gameMenu = gameMenu;
		addKeyListener(new AL());
		addMouseListener(new ML());
		addMouseMotionListener(new MML());
		setLayout(null);
	}

	@Override
	public void paint(Graphics g) {
		try {
			requestFocusInWindow();
			if (!visualManager.drawNow()) {
				return;
			}
			Graphics2D g2 = (Graphics2D) g;

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			View view = visualManager.getRenderedView();
			view.draw(g2);

			drawGameMenu(g2);
			// drawInfoScreen(g2);
			drawGameIsPaused(g2);
		} catch (ConcurrentModificationException e) {
		}
	}

	private void drawGameIsPaused(Graphics2D g2) {
		if (core.gameIsPaused()) {
			g2.setColor(Color.YELLOW);
			Font f = new Font(Font.SANS_SERIF, Font.BOLD, 20);
			g2.setFont(f);
			g2.drawString(
					"PAUSED",
					(GameSettings.GAME_FRAME_WIDTH - GameSettings.MENU_WIDTH) / 2,
					GameSettings.GAME_FRAME_HEIGHT / 2);
		}
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

	private void drawGameMenu(Graphics2D g2) {
		gameMenu.draw(g2, -1, -1);
	}

	public boolean infoScreenIsShown() {
		return currentInfoScreen != null;
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

	private class ML implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			inputHandler.mouseClicked(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			inputHandler.mousePressed(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			inputHandler.mouseReleased(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}

	private class MML implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			inputHandler.mouseDragged(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			inputHandler.mouseMoved(e);
		}

	}
}
