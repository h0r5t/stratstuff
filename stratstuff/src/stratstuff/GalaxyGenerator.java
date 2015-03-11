package stratstuff;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class GalaxyGenerator {

	public static Galaxy generateGalaxy(String galaxyName) {
		Galaxy g = new Galaxy(galaxyName, 100, 100);

		for (int i = -5; i < 6; i++) {
			for (int o = -5; o < 6; o++) {
				Sector s = new Sector(g, i, o);

				BufferedImage image = TextureGenerator.generateImage(
						new ColorData(Color.ORANGE,
								GameSettings.COLOR_VARIATION, 0), 5, false,
						TextureShape.circle);

				SpacePosition pos = new SpacePosition(s, 300, 300);

				Planet p = new Planet(UniqueIDFactory.getID(), "planet", pos,
						image);
				s.addObject(p);

				pos = new SpacePosition(s, 600, 600);

				Starship ship = new Starship(UniqueIDFactory.getID(), "ship",
						pos, 5);
				s.addObject(ship);
				g.setSector(s);
			}
		}

		return g;
	}
}
