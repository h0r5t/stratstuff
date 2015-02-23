package stratstuff;

import java.io.File;
import java.io.IOException;
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
		world = loadAndAddUnits(world, worldName);
		world = loadAndAddItems(world, worldName);

		return world;
	}

	private static World loadAndAddItems(World world, String worldName) {
		try {
			Scanner scanner = new Scanner(getItemsFile(worldName));

			String line;
			String[] splitLine;
			while (scanner.hasNextLine()) {
				line = scanner.nextLine().trim();
				splitLine = line.split(" ");
				int uniqueID = Integer.parseInt(splitLine[0]);
				int itemType = Integer.parseInt(splitLine[1]);
				int linkedObjUID = Integer.parseInt(splitLine[2]);
				int ownerUnitID = Integer.parseInt(splitLine[3]);
				String infoText = splitLine[4];

				Item item = new Item(world, uniqueID, itemType, linkedObjUID,
						ownerUnitID, infoText);
				world.addItem(item);
				UniqueIDFactory.increment();
			}

			scanner.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return world;
	}

	private static World loadAndAddUnits(World world, String worldName) {
		try {
			Scanner scanner = new Scanner(getUnitsFile(worldName));

			String line;
			String[] splitLine;
			while (scanner.hasNextLine()) {
				line = scanner.nextLine().trim();
				splitLine = line.split(" ");
				int uniqueID = Integer.parseInt(splitLine[0]);
				int unitType = Integer.parseInt(splitLine[1]);
				int objUID = Integer.parseInt(splitLine[2]);

				Unit unit = new Unit(world, uniqueID, unitType, objUID);
				world.addUnit(unit);
				UniqueIDFactory.increment();
			}

			scanner.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

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
				int uniqueID = Integer.parseInt(splitLine[1]);

				MovingObject object = new MovingObject(objectID, w, uniqueID);
				w.spawnObject(object, w.getWP(x, y, z));
				UniqueIDFactory.increment();
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
		saveItems(main, worldName);
		saveUnits(main, worldName);
		generateItemsTxt();
	}

	private static void generateItemsTxt() {
		File itemsTxt = new File(FileSystem.WORLDS_DIR + "/test/items.txt");
		if (!itemsTxt.exists()) {
			try {
				itemsTxt.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
			}
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void saveObjects(Core main, String worldName) {
		try {
			File f = getObjectsFile(worldName);

			PrintWriter writer = new PrintWriter(f);

			ArrayList<MovingObject> objects = new ArrayList<MovingObject>(main
					.getObjectManager().getUnits().values());

			for (MovingObject object : objects) {
				String out = object.save() + "\n";
				writer.append(out);
			}

			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void saveItems(Core main, String worldName) {
		try {
			File f = getItemsFile(worldName);

			PrintWriter writer = new PrintWriter(f);

			ArrayList<Item> itemlist = main.getItemManager().getItemList();

			for (Item item : itemlist) {
				String out = item.save() + "\n";
				writer.append(out);
			}

			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void saveUnits(Core main, String worldName) {
		try {
			File f = getUnitsFile(worldName);

			PrintWriter writer = new PrintWriter(f);

			ArrayList<Unit> unitlist = main.getUnitManager().getUnitList();

			for (Unit unit : unitlist) {
				String out = unit.save() + "\n";
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

	private static File getItemsFile(String worldName) {
		return new File(FileSystem.WORLDS_DIR + "/" + worldName + "/items.txt");
	}

	private static File getUnitsFile(String worldName) {
		return new File(FileSystem.WORLDS_DIR + "/" + worldName + "/units.txt");
	}
}
