package stratstuff;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemManager implements Updatable {

	private HashMap<Integer, Item> itemMap;

	public ItemManager(Core core) {
		itemMap = new HashMap<Integer, Item>();
	}

	public void addItem(Item item) {
		itemMap.put(item.getUniqueID(), item);
	}

	public Item getItem(int uid) {
		return itemMap.get(uid);
	}

	@Override
	public void update() {
	}

	public ArrayList<Item> getItemList() {
		return new ArrayList<Item>(itemMap.values());
	}

}
