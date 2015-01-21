package stratstuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
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
			currentMenu = currentMenu.getParent();
		} else {
			String c = String.valueOf((char) keyCode);
			c = c.toLowerCase();
			if (currentMenu.isLeaf(c)) {
				GameMenuItem item = currentMenu.getLeaf(c);
				if (item.getType().equals(GameMenuItem.TYPE_SINGLE)) {
					String frontendMessage = FrontendMessaging
							.menuInputMessage(item.getName(), item.getType(),
									"todo");
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
	}

	@Override
	public void draw(Graphics2D g, int xinpixels, int yinpixels) {
		g.setColor(Color.BLACK);
		g.fillRect(GameSettings.MENU_X - 35, 0, 500,
				GameSettings.GAME_FRAME_HEIGHT);

		drawMenuData(g);
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
}
