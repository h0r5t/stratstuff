package stratstuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class ContextCommandsMenu implements UIElement {

	private ArrayList<ContextCommand> listOfCommands;
	private int posX;
	private int posY;
	private HashMap<Integer, ContextCommand> boxMap;
	private int boxHeight = 0, boxWidth = 150;
	private ContextCommand highlightedCommand;
	private WorldPoint worldPoint;

	public ContextCommandsMenu(ArrayList<ContextCommand> listOfCommands, int x, int y, WorldPoint wp) {
		this.listOfCommands = listOfCommands;
		this.posX = x;
		this.posY = y;
		this.boxMap = new HashMap<Integer, ContextCommand>();
		this.worldPoint = wp;
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {

		boxHeight = listOfCommands.size() * 10 + 10;
		g.setColor(Color.WHITE);
		g.fillRect(posX, posY, 150, boxHeight);

		g.setColor(Color.BLACK);
		g.drawRect(posX, posY, 150, boxHeight);

		int fontPosX = posX + 10;
		int fontPosY = posY + 15;
		for (ContextCommand command : listOfCommands) {
			if (command.equals(highlightedCommand)) {
				g.setColor(Color.CYAN);
				g.fillRect(fontPosX, fontPosY - 10, 130, 10);
			}
			g.setColor(Color.BLACK);
			g.drawString(command.getMethodName(), fontPosX, fontPosY);
			for (int i = fontPosY - 11; i < fontPosY; i++)
				boxMap.put(i, command);
			fontPosY += 10;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// user clicked in the box
		int clickY = e.getY();

		ContextCommand selectedCommand = boxMap.get(clickY);
		selectedCommand.run(worldPoint);
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
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean overridesInput(int x, int y) {
		if (x > posX && x < posX + boxWidth && y > posY && y < posY + boxHeight)
			return true;
		return false;
	}

	@Override
	public UIElementType getType() {
		return UIElementType.Tooltip;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		highlightedCommand = boxMap.get(e.getY());
	}

}
