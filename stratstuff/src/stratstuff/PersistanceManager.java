package stratstuff;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class PersistanceManager {

	// Ground save format: 1 1 1 ... 2 3 4 \n 2 3....
	// Rest is for now saved in a list form with: data x y z

	public static World load(Core main, String worldName) {
		World world = new World(main);

		world = loadGroundIDs(world, worldName);
		world = loadAndAddElements(world, worldName);
		world = loadAndAddObjects(world, worldName);

		return world;
	}

	private static World loadGroundIDs(World w, String worldName) {
		try {
			for (int z = 0; z < GameSettings.WORLD_DEPTH; z++) {
				File f = getLayerFile(worldName, z);

				Scanner scanner = new Scanner(f);
				scanner.useDelimiter(" ");

				String temp;
				int x = 0;
				int y = 0;
				loop: while (scanner.hasNext()) {
					temp = scanner.next().trim();

					if (x == 120) {
						y++;
						x = 0;
					}

					int groundID = Integer.parseInt(temp);
					w.addWorldPoint(new WorldPoint(x, y, z, groundID));

					if (x == GameSettings.WORLD_WIDTH - 1
							&& y == GameSettings.WORLD_HEIGHT - 1) {
						break loop;
					}

					x++;
				}

				scanner.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return w;
	}

	private static World loadAndAddElements(World w, String worldName) {
		try {
			Scanner scanner = new Scanner(getElementsFile(worldName));

			String line;
			String[] splitLine;
			while (scanner.hasNextLine()) {
				line = scanner.nextLine().trim();
				splitLine = line.split(" ");
				int x = Integer.parseInt(splitLine[splitLine.length - 3]);
				int y = Integer.parseInt(splitLine[splitLine.length - 2]);
				int z = Integer.parseInt(splitLine[splitLine.length - 1]);
				// this allows to add info later if possible, dunno
				int elementID = Integer.parseInt(splitLine[0]);
				w.setElementForWP(true, w.getWP(x, y, z), elementID);
			}

			scanner.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return w;
	}

	private static World loadAndAddObjects(World w, String worldName) {
		try {
			Scanner scanner = new Scanner(getObjectsFile(worldName));

			String line;
			String[] splitLine;
			while (scanner.hasNextLine()) {
				line = scanner.nextLine().trim();
				splitLine = line.split(" ");
				int x = Integer.parseInt(splitLine[splitLine.length - 3]);
				int y = Integer.parseInt(splitLine[splitLine.length - 2]);
				int z = Integer.parseInt(splitLine[splitLine.length - 1]);
				// this allows to add info later if possible, dunno
				int objectID = Integer.parseInt(splitLine[0]);

				MovingObject object = MovingObject.createFromType(objectID, w);
				w.spawnObject(object, w.getWP(x, y, z));
			}

			scanner.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return w;
	}

	public static void save(Core main, String worldName) {
		saveGroundIDs(main.getWorld(), worldName);
		saveElements(main.getWorld(), worldName);
		saveObjects(main, worldName);
	}

	private static void saveGroundIDs(World w, String worldName) {
		try {
			for (int z = 0; z < GameSettings.WORLD_DEPTH; z++) {
				File f = getLayerFile(worldName, z);

				PrintWriter writer = new PrintWriter(f);

				for (int y = 0; y < GameSettings.WORLD_HEIGHT; y++) {
					for (int x = 0; x < GameSettings.WORLD_WIDTH; x++) {
						String out = w.getWP(x, y, z).getGround() + " ";
						writer.append(out);
					}
					writer.append("\n");
				}

				writer.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void saveElements(World w, String worldName) {
		try {
			File f = getElementsFile(worldName);
			PrintWriter writer = new PrintWriter(f);

			for (int z = 0; z < GameSettings.WORLD_DEPTH; z++) {
				for (int y = 0; y < GameSettings.WORLD_HEIGHT; y++) {
					for (int x = 0; x < GameSettings.WORLD_WIDTH; x++) {
						int elementID = w.getWP(x, y, z).getAttachedElement();
						if (elementID != -1) {
							// defined
							String out = elementID + " " + x + " " + y + " "
									+ z + "\n";
							writer.append(out);
						}
					}

				}

				writer.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void saveObjects(Core main, String worldName) {
		try {
			File f = getObjectsFile(worldName);

			PrintWriter writer = new PrintWriter(f);

			ArrayList<MovingObject> objects = new ArrayList<MovingObject>(main
					.getUnitManager().getUnits().values());

			for (MovingObject object : objects) {
				String out = object.save() + "\n";
				writer.append(out);
			}

			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static File getLayerFile(String worldName, int i) {
		return new File(FileSystem.WORLDS_DIR + "/" + worldName + "/" + i
				+ ".layer");
	}

	private static File getElementsFile(String worldName) {
		return new File(FileSystem.WORLDS_DIR + "/" + worldName
				+ "/elements.txt");
	}

	private static File getObjectsFile(String worldName) {
		return new File(FileSystem.WORLDS_DIR + "/" + worldName
				+ "/objects.txt");
	}
}
