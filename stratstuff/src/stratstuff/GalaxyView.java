package stratstuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GalaxyView implements View {
	private Galaxy galaxy;
	private Core core;

	private String posInfo = "";
	private int infoX = 0;
	private int infoY = 0;

	private DragSelectionBox dragBox;
	private SelectionMenu selectionMenu;

	public GalaxyView(Core core, Galaxy galaxy) {
		this.galaxy = galaxy;
		this.core = core;
		selectionMenu = new SelectionMenu();
	}

	@Override
	public void draw(Graphics2D g) {
		galaxy.draw(g, 0, 0);

		g.setColor(Color.GRAY);
		g.drawString(posInfo, infoX, infoY);

		if (dragBox != null)
			dragBox.draw(g, 0, 0);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keysArePressed(DefaultHashMap<Integer, Boolean> keyMap) {
		if (keyMap.getDefault(KeyEvent.VK_W, false)) {
			galaxy.getCamera().up();
		}
		if (keyMap.getDefault(KeyEvent.VK_S, false)) {
			galaxy.getCamera().down();
		}
		if (keyMap.getDefault(KeyEvent.VK_D, false)) {
			galaxy.getCamera().right();
		}
		if (keyMap.getDefault(KeyEvent.VK_A, false)) {
			galaxy.getCamera().left();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		SpacePosition pos = galaxy.getCamera().screenPosToSpacePos(x, y);
		for (Starship ship : selectionMenu.getSelectedStarships()) {
			core.getTaskManager().runTask(
					new MoveTask_FO_P2P(galaxy, core.getTaskManager(), ship,
							pos), UniqueIDFactory.getID());
			dragBox = null;
		}
	}

	private void updateSpacePositionString(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		SpacePosition pos = galaxy.getCamera().screenPosToSpacePos(x, y);
		posInfo = "S:" + pos.getSector().getXPos() + ","
				+ pos.getSector().getYPos() + " Mic:" + pos.getMicX() + ","
				+ pos.getMicY() + " Mac:" + pos.getMacX() + "," + pos.getMacY();
		infoX = x;
		infoY = y;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		updateSpacePositionString(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		updateSpacePositionString(e);

		// update drag box if mouse was pressed and not released yet
		if (dragBox != null) {
			dragBox.setEndX(e.getX());
			dragBox.setEndY(e.getY());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (dragBox == null)
			dragBox = new DragSelectionBox(galaxy, e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (dragBox.isBigEnough()) {
			selectionMenu.setSelectedStarships(dragBox
					.getAllSelectedStarships());
			dragBox = null;
		}
	}
}
