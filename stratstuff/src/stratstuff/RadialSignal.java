package stratstuff;

import java.awt.Color;
import java.awt.Graphics2D;

public class RadialSignal extends Signal {

	private Core core;
	private WorldPoint source;
	private String message;
	private Color color = Color.RED;
	private int currentRadius = 0;
	private int radiusIncrease = 5;
	private final int maxRadius = 255;

	public RadialSignal(Core core, WorldPoint source, String message) {
		super(source);
		this.core = core;
		this.source = source;
		this.message = message;
		source.addMicroObject(this);
	}

	private Color lowerAlpha(Color c) {
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()
				- radiusIncrease);
	}

	@Override
	public void update() {
		currentRadius += radiusIncrease;
		if (currentRadius > maxRadius) {
			source.removeMicroObject(this);
			core.getWorld().removeMicroObject(this);
			return;
		}
		color = lowerAlpha(color);
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		g.setColor(color);
		g.drawOval(xinpixels - currentRadius, yinpixels - currentRadius,
				currentRadius * 2, currentRadius * 2);
	}

}
