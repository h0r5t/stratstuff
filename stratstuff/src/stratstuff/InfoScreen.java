package stratstuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Scanner;

public class InfoScreen implements Drawable {

	private String title = "";
	private ArrayList<InfoScreenBodyElement> bodyElements;
	private int x, y;

	public InfoScreen(String infoString, int x, int y) {
		this.x = x;
		this.y = y;
		bodyElements = new ArrayList<InfoScreenBodyElement>();

		infoString = infoString.replaceAll("&", " ");
		infoString = infoString.replaceAll("<newline>", "\n");
		Scanner scanner = new Scanner(infoString);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.startsWith("<title>")) {
				title = line.replace("<title>", "");
			} else if (line.startsWith("<body>")) {
				// parse body elements
			}
		}
		scanner.close();
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y, 500, 500);

		g.setColor(Color.YELLOW);
		g.drawString(title, x + 50, y + 50);

		g.setColor(Color.WHITE);
		int tempy = y + 100;
		// TODO
		// Scanner scanner = new Scanner(body);
		// while (scanner.hasNextLine()) {
		// g.drawString(scanner.nextLine(), x + 50, tempy);
		// tempy += 15;
		// }
		// scanner.close();
	}
}
