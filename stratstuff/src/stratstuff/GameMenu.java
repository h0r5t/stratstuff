package stratstuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class GameMenu implements Drawable {

	private GameMenuTree menuTree;
	private GameMenuTree currentMenu;
	private Core core;
	private GameMenuItem currentItemForSelection;

	public GameMenu(Core core) {
		this.core = core;
		try {
			loadMenuData();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadMenuData() throws IOException {
		File menuDataFile = new File(FileSystem.MENU_DATA_FILE);

		Scanner scanner = new Scanner(menuDataFile);

		menuTree = new GameMenuTree("root", "", null);
		menuTree.setParentTree(menuTree);

		String temp;
		GameMenuTree currentParent = menuTree;
		Stack<GameMenuTree> parentStack = new Stack<GameMenuTree>();
		parentStack.push(currentParent);
		while (scanner.hasNextLine()) {
			temp = scanner.nextLine().trim();

			if (temp.equals("{")) {
				continue;
			}

			if (temp.equals("}")) {
				if (parentStack.size() > 1) {
					parentStack.pop();
				}
				if (parentStack.peek().getName().equals("root")) {
					currentParent = menuTree;
				} else if (parentStack.size() > 1) {
					currentParent = parentStack.pop();
				}
				continue;
			}

			String[] split = temp.split(":");

			if (split[0].startsWith(">")) {
				String itemType = split[1].split(" ")[1];
				String itemHotkey = split[0].replaceAll(">", "");
				String itemName = split[1].split(" ")[0];
				menuTree.addLeafAt(currentParent.getName(), new GameMenuItem(
						itemType, itemHotkey, itemName));
			} else {
				String treeName = split[1];
				String treeHotkey = split[0];
				GameMenuTree subTree = new GameMenuTree(treeName, treeHotkey,
						currentParent);
				menuTree.addSubTreeAt(currentParent.getName(), subTree);
				currentParent = subTree;
				parentStack.push(currentParent);
			}

		}
		scanner.close();

		currentMenu = menuTree;
	}

	public void evaluateInput(int keyCode) {
		if (keyCode == KeyEvent.VK_ESCAPE) {
			if (core.getCanvas().infoScreenIsShown()) {
				core.getCanvas().setInfoScreen(null);
			} else if (core.getInputManager().selectionAreaIsOn()) {
				core.getInputManager().endSelectionArea();
			} else {
				currentMenu = currentMenu.getParent();
			}
		} else {
			String c = String.valueOf((char) keyCode);
			c = c.toLowerCase();
			if (currentMenu.isLeaf(c)) {
				GameMenuItem item = currentMenu.getLeaf(c);
				if (item.getType().equals(GameMenuItem.TYPE_SINGLE)) {
					String positionInfo = core.getCursor().getX() + ","
							+ core.getCursor().getY() + ","
							+ core.getCamera().getLayer();
					String frontendMessage = FrontendMessaging
							.menuInputMessage(item.getName(), item.getType(),
									positionInfo);
					core.getFrontendAdapter().addToQueue(frontendMessage);
				} else {
					currentItemForSelection = item;
					core.getInputManager().startSelectionArea();
				}
			} else {
				GameMenuTree tree = currentMenu.getNextMenu(c);
				if (tree != null) {
					currentMenu = tree;
				}
			}
		}
	}

	public void areaWasSelected(Area3D area) {
		String frontendMessage = FrontendMessaging.menuInputMessage(
				currentItemForSelection.getName(),
				currentItemForSelection.getType(), area.toAreaString());
		core.getFrontendAdapter().addToQueue(frontendMessage);
		// needs to be changed TEST ONLY

		World w = core.getWorld();
		WorldPoint p = w.getWP(area.getX(), area.getY(), area.getZ());
		w.removeElementFromWP(p);
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		g.setColor(Color.BLACK);
		g.fillRect(GameSettings.MENU_X - 35, 0, 500,
				GameSettings.GAME_FRAME_HEIGHT);

		drawMenuData(g);
		drawCursorPositionInfo(g);
	}

	private void drawMenuData(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.drawString(currentMenu.getName(), GameSettings.MENU_X, 25);

		g.setColor(Color.GREEN);
		g.drawString("Menus", GameSettings.MENU_X, 45);

		int y = 75;
		for (GameMenuTree tree : currentMenu.getSubTrees()) {
			g.setColor(Color.GREEN);
			g.drawString(tree.getHotKey(), GameSettings.MENU_X, y);
			g.setColor(Color.WHITE);
			g.drawString(tree.getName(), GameSettings.MENU_X + 50, y);
			y += 25;
		}

		y += 100;

		g.setColor(Color.GREEN);
		g.drawString("Actions", GameSettings.MENU_X, y);

		y += 30;

		for (GameMenuItem leaf : currentMenu.getLeaves()) {
			g.setColor(Color.GREEN);
			g.drawString(leaf.getHotkey(), GameSettings.MENU_X, y);
			g.setColor(Color.WHITE);
			g.drawString(leaf.getName(), GameSettings.MENU_X + 50, y);
			y += 25;
		}
	}

	private void drawCursorPositionInfo(Graphics2D g) {
		GameCursor cursor = core.getCursor();
		int x = cursor.getX();
		int y = cursor.getY();
		int z = core.getCamera().getLayer();

		WorldPoint cursorWP = core.getWorld().getWP(x, y, z);
		int groundID = cursorWP.getGround();
		int elementID = cursorWP.getAttachedElement();
		ArrayList<MovingObject> objects = cursorWP.getAttachedMovingObjects();

		String groundText = Ground.getName(groundID);
		String elementText = "";
		if (elementID != -1) {
			elementText = Element.getName(elementID);
		}
		ArrayList<String> objectNames = new ArrayList<String>();
		for (MovingObject obj : objects) {
			objectNames.add(obj.getName() + " " + obj.getUniqueID());
		}

		int amountOfInfo = 2;
		if (objectNames.size() > 0) {
			amountOfInfo += objectNames.size();
		} else {
			amountOfInfo = 3;
		}

		int xpos = GameSettings.MENU_X;
		int xposinfo = GameSettings.MENU_X + 55;
		int ypos = GameSettings.GAME_FRAME_HEIGHT - amountOfInfo * 25 - 50;

		g.setColor(Color.YELLOW);
		g.drawString("Objects: ", xpos, ypos);

		for (String s : objectNames) {
			g.drawString(s, xposinfo, ypos);
			ypos += 25;
		}

		if (objectNames.size() == 0) {
			ypos += 25;
		}

		g.setColor(Color.WHITE);
		g.drawString("Element: " + elementText, xpos, ypos);

		ypos += 25;

		g.setColor(Color.GREEN);
		g.drawString("Ground: " + groundText, xpos, ypos);

	}
}
