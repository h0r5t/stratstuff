package stratstuff;

import java.util.ArrayList;

public class GameMenuTree {

	private String name;
	private String hotKey;
	private ArrayList<GameMenuTree> subTrees;
	private ArrayList<GameMenuItem> leaves;
	private GameMenuTree parentTree;

	public GameMenuTree(String name, String hotKey, GameMenuTree parentTree) {
		this.name = name;
		this.hotKey = hotKey;
		this.parentTree = parentTree;

		subTrees = new ArrayList<GameMenuTree>();
		leaves = new ArrayList<GameMenuItem>();
	}

	public void addSubTreeAt(String nameOfParent, GameMenuTree tree) {
		if (nameOfParent.equals(name)) {
			subTrees.add(tree);
		} else {
			for (GameMenuTree t : subTrees) {
				t.addSubTreeAt(nameOfParent, tree);
			}
		}
	}

	public void addLeafAt(String nameOfParent, GameMenuItem item) {
		if (nameOfParent.equals(name)) {
			leaves.add(item);
		} else {
			for (GameMenuTree t : subTrees) {
				t.addLeafAt(nameOfParent, item);
			}
		}
	}

	public String getName() {
		return name;
	}

	public String getHotKey() {
		return hotKey;
	}

	public GameMenuTree getParent() {
		return parentTree;
	}

	public ArrayList<GameMenuTree> getSubTrees() {
		return subTrees;
	}

	public ArrayList<GameMenuItem> getLeaves() {
		return leaves;
	}

	public GameMenuTree getNextMenu(String c) {
		for (GameMenuTree subTree : subTrees) {
			if (c.equals(subTree.hotKey)) {
				return subTree;
			}
		}
		return null;
	}

	public GameMenuItem getLeaf(String c) {
		for (GameMenuItem item : leaves) {
			if (c.equals(item.getHotkey())) {
				return item;
			}
		}
		return null;
	}

	public boolean isLeaf(String c) {
		for (GameMenuItem item : leaves) {
			if (item.getHotkey().equals(c)) {
				return true;
			}
		}
		return false;
	}

	public void print() {
		System.out.println(hotKey + " " + name);
		for (GameMenuTree tree : subTrees) {
			tree.print();
		}
		for (GameMenuItem item : leaves) {
			System.out.println("Item : " + item.getHotkey() + " "
					+ item.getName() + " " + item.getType());
		}
	}
}
