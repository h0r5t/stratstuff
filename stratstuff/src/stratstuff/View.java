package stratstuff;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface View {

	public void draw(Graphics2D g);

	public void keyReleased(KeyEvent e);

	public void keysArePressed(DefaultHashMap<Integer, Boolean> keyMap);

	public void mouseClicked(MouseEvent e);

	public void mouseMoved(MouseEvent e);

	public void mousePressed(MouseEvent e);

	public void mouseReleased(MouseEvent e);

	public void mouseDragged(MouseEvent e);
}
