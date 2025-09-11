package lol.roxxane.roxxys_survival_core.util;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class New {
	@SuppressWarnings("unchecked")
	public static <K, V> HashMap<K, V> map(Object... objects) {
		var hashmap = new HashMap<K, V>();
		Object key = null;
		var is_key = true;
		for (var object : objects) {
			if (is_key) {key = object; is_key = false;}
			else { hashmap.put((K) key, (V) object); is_key = true; }
		}
		return hashmap;
	}
	@SafeVarargs
	public static <T> ArrayList<T> list(T... elements) {
		return new ArrayList<>(List.of(elements));
	}
	public static CommentedConfig config(Map<String, Object> map) {
		return TomlFormat.newConfig(() -> Map.copyOf(map));
	}
	public static CommentedConfig config(Object... objects) {
		return TomlFormat.newConfig(() -> map(objects));
	}
}
