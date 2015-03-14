package stratstuff;

public class GalaxyGenerator {

	public static Galaxy generateGalaxy(String galaxyName) {
		Galaxy g = new Galaxy(galaxyName, 100, 100);

		for (int i = -2; i < 3; i++) {
			for (int o = -2; o < 3; o++) {
				Sector s = new Sector(g, i, o);

				int amount = (int) (Math.random() * 20 + 10);

				for (int a = 0; a < amount; a++) {
					Planet p = generatePlanet(s);
					s.addObject(p);
				}

				amount = (int) (Math.random() * 3 + 2);

				for (int a = 0; a < amount; a++) {
					Starship ship = generateStarship(s);
					s.addObject(ship);
				}

				g.setSector(s);
			}
		}

		return g;
	}

	private static Planet generatePlanet(Sector s) {
		int colorCode = ColorGenerator.generatePlanetColorCode();
		int diameter = (int) (Math.random() * 30 + 20);

		Planet p = new Planet(UniqueIDFactory.getID(), "planet",
				generateRandomPosition(s), colorCode, diameter);
		return p;
	}

	private static Starship generateStarship(Sector s) {
		Starship ship = new Starship(UniqueIDFactory.getID(), "ship",
				generateRandomPosition(s), 5);
		return ship;
	}

	private static SpacePosition generateRandomPosition(Sector s) {
		int x = (int) (Math.random() * Sector.sectorWidth - 50);
		int y = (int) (Math.random() * Sector.sectorHeight - 50);

		SpacePosition pos = new SpacePosition(s, x, y);

		return pos;
	}
}
