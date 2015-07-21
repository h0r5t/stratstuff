package stratstuff;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public interface UIElement extends Drawable, KeyListener, MouseListener, MouseMotionListener {

	public boolean overridesInput(int pixelX, int pixelY);

	public UIElementType getType();

}
