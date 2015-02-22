package stratstuff;

import java.awt.Graphics2D;
import java.util.HashMap;

public class InfoScreenBodyElement implements Drawable {
	// use this to make infoscreen interactive
	// when an action is selected, send input message back to frontend

	private String myText;
	private HashMap<String, InfoScreenBodyAction> hotkeyActionsMap;

	public InfoScreenBodyElement(String bodyText) {
		this.myText = bodyText;
		hotkeyActionsMap = new HashMap<String, InfoScreenBodyAction>();
	}

	public void addAction(String hotkey, InfoScreenBodyAction action) {
		hotkeyActionsMap.put(hotkey, action);
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {

	}

}
