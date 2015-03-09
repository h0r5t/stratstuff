package stratstuff;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class PersistanceManager {

	// Ground save format: 1 1 1 ... 2 3 4 \n 2 3....
	// Rest is for now saved in a list form with: data x y z

	public static World load(Core main, String worldName) {
		int[] worldSize = loadWorldSize(worldName);
		int width = worldSize[0];
		int height = worldSize[1];
		int depth = worldSize[2];

		World world = new World(main, worldName, width, height, depth);

		world = loadGroundIDs(world, worldName);
		world = loadAndAddElements(world, worldName);
		world = loadAndAddObjects(world, worldName);
		world = loadAndAddUnits(main, world, worldName);
		world = loadAndAddItems(world, worldName);

		return world;
	}

	private static int[] loadWorldSize(String worldName) {
		int[] size = new int[3];
		try {
			Scanner scanner = new Scanner(getSizeFile(worldName));

			String temp;
			int i = 0;
			while (scanner.hasNextLine()) {
				temp = scanner.nextLine();
				size[i] = Integer.parseInt(temp);
				i++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return size;
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

	private static World loadAndAddUnits(Core core, World world,
			String worldName) {
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
				String designName = splitLine[3];

				Unit unit = new Unit(core, world, uniqueID, unitType, objUID,
						designName);
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
			for (int z = 0; z < w.getDepth(); z++) {
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

					if (x == w.getWidth() - 1 && y == w.getHeight() - 1) {
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

	public static void save(Core main, World world, String worldName) {
		saveWorldSize(world, worldName);
		saveGroundIDs(world, worldName);
		saveElements(world, worldName);
		saveObjects(main, world, worldName);
		saveItems(main, world, worldName);
		saveUnits(main, world, worldName);
		generateItemsTxt(worldName);
	}

	private static void saveWorldSize(World world, String worldName) {
		try {
			File f = getSizeFile(worldName);

			PrintWriter writer = new PrintWriter(f);

			writer.append(world.getWidth() + "\n");
			writer.append(world.getHeight() + "\n");
			writer.append(world.getDepth() + "");

			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void generateItemsTxt(String name) {
		File itemsTxt = new File(FileSystem.WORLDS_DIR + "/" + name
				+ "/items.txt");
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
			for (int z = 0; z < w.getDepth(); z++) {
				File f = getLayerFile(worldName, z);

				PrintWriter writer = new PrintWriter(f);

				for (int y = 0; y < w.getHeight(); y++) {
					for (int x = 0; x < w.getWidth(); x++) {
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

			for (int z = 0; z < w.getDepth(); z++) {
				for (int y = 0; y < w.getHeight(); y++) {
					for (int x = 0; x < w.getWidth(); x++) {
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

	private static void saveObjects(Core main, World world, String worldName) {
		try {
			File f = getObjectsFile(worldName);

			PrintWriter writer = new PrintWriter(f);

			ArrayList<MovingObject> objects = new ArrayList<MovingObject>(main
					.getObjectManager().getUnits().values());

			for (MovingObject object : objects) {
				if (object.getWorld() != world)
					continue;
				String out = object.save() + "\n";
				writer.append(out);
			}

			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void saveItems(Core main, World world, String worldName) {
		try {
			File f = getItemsFile(worldName);

			PrintWriter writer = new PrintWriter(f);

			ArrayList<Item> itemlist = main.getItemManager().getItemList();

			for (Item item : itemlist) {
				if (item.getWorld() != world)
					continue;
				String out = item.save() + "\n";
				writer.append(out);
			}

			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void saveUnits(Core main, World world, String worldName) {
		try {
			File f = getUnitsFile(worldName);

			PrintWriter writer = new PrintWriter(f);

			ArrayList<Unit> unitlist = main.getUnitManager().getUnitList();

			for (Unit unit : unitlist) {
				if (unit.getWorld() != world)
					continue;
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

	private static File getSizeFile(String worldName) {
		return new File(FileSystem.WORLDS_DIR + "/" + worldName + "/size.info");
	}
}
