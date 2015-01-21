package stratstuff;

import java.util.HashMap;

public class DefaultHashMap<K, V> extends HashMap<K, V> {
	public V getDefault(K key, V defaultValue) {
		if (containsKey(key)) {
			return get(key);
		}

		return defaultValue;
	}
}
