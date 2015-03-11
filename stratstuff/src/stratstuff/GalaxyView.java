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

	public GalaxyView(Core core, Galaxy galaxy) {
		this.galaxy = galaxy;
		this.core = core;
	}

	@Override
	public void draw(Graphics2D g) {
		galaxy.draw(g, 0, 0);

		g.setColor(Color.GRAY);
		g.drawString(posInfo, infoX, infoY);
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
		Starship ship = galaxy.getSector(0, 0).getRandomStarship();
		core.getTaskManager().runTask(
				new MoveTask_FO(galaxy, core.getTaskManager(), ship, pos),
				UniqueIDFactory.getID());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		SpacePosition pos = galaxy.getCamera().screenPosToSpacePos(x, y);
		posInfo = "S:" + pos.getSector().getXPos() + ","
				+ pos.getSector().getYPos() + " Mic:" + pos.getMicX() + ","
				+ pos.getMicY() + " Mac:" + pos.getMacX() + "," + pos.getMacY();
		infoX = x;
		infoY = y;
	}
}
