package stratstuff;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemManager implements Updatable {

	private HashMap<Integer, Item> itemMap;

	public ItemManager(Core core) {
		itemMap = new HashMap<Integer, Item>();
	}

	public void removeItem(Item item) {
		itemMap.remove(item.getUniqueID());
	}

	public void addItem(Item item) {
		itemMap.put(item.getUniqueID(), item);
	}

	public Item getItem(int itemUID) {
		return itemMap.get(itemUID);
	}

	public Item getItemByObjUID(int objUID) {
		for (Item i : getItemList()) {
			if (i.getLinkedObjUniqueID() == objUID) {
				return i;
			}
		}
		return null;
	}

	@Override
	public void update() {
	}

	public ArrayList<Item> getItemList() {
		return new ArrayList<Item>(itemMap.values());
	}

}
