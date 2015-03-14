package stratstuff;

import java.awt.Color;
import java.awt.Graphics2D;

public class SelectionIndicator implements Drawable, Updatable {

	public static final int TYPE_STARSHIP = 0;

	private int myType;
	private DynamicTexture myTexture;
	private int currentRotationGoal = 45;

	public SelectionIndicator(int type) {
		myType = type;

		myTexture = new DynamicTexture(
				TextureGenerator.generateSelectionIndicatorImage(), 1, 1);
		myTexture.setRotationGoal(currentRotationGoal);
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		if (myType == TYPE_STARSHIP) {
			// g.drawImage(myTexture.getImage(), xinpixels - 15 - 45 / 2,
			// yinpixels - 15 - 45 / 2, null);
			g.setColor(Color.ORANGE);
			g.drawOval(xinpixels - 15, yinpixels - 15, 45, 45);
		}
	}

	@Override
	public void update() {
		// myTexture.update();
		if (myTexture.hasTurned()) {
			currentRotationGoal += 45;
			if (currentRotationGoal == 360) {
				currentRotationGoal = 0;
			}
			myTexture.setRotationGoal(currentRotationGoal);
		}
	}

}
