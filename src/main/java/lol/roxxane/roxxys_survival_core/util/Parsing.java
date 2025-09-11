package lol.roxxane.roxxys_survival_core.util;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unused")
public class Parsing {
	public static boolean is_id(Object o) {
		return o instanceof String string && Id.is(string);
	}
	public static boolean is_item(Object o) {
		return o instanceof String string && is_id(string) && ForgeRegistries.ITEMS.containsKey(Id.of(string));
	}
	public static boolean is_stack(Object o) {
		if (o instanceof String) {
			return is_item(o);
		} else if (o instanceof Map<?,?> map) {
			return validate_key(map, "item", Parsing::is_item) &&
				validate_key(map, "tag", value -> value instanceof Map<?,?>);
		}
		return false;
	}
	public static boolean is_collection(Object o, Predicate<Object> predicate) {
		return o instanceof Collection<?> collection && collection.stream().allMatch(predicate);
	}
	public static boolean is_map(Object o, Predicate<Object> key_predicate, Predicate<Object> value_predicate) {
		if (o instanceof Map<?,?> map) {
			return map.keySet().stream().allMatch(key_predicate) && map.values().stream().allMatch(value_predicate);
		} else if (o instanceof UnmodifiableConfig config)
			return is_map(config.valueMap(), key_predicate, value_predicate);
		return false;
	}
	public static boolean validate_key(Map<?,?> map, Object key, Predicate<Object> value_predicate) {
		return map.containsKey(key) && value_predicate.test(map.get(key));
	}
	public static ResourceLocation as_id(Object o) {
		return Id.of((String) o);
	}
	public static Item as_item(Object o) {
		return requireNonNull(ForgeRegistries.ITEMS.getValue(as_id(o)));
	}
	public static ItemStack as_stack(Object o) {
		if (o instanceof String)
			return new ItemStack(as_item(o));
		else if (o instanceof Map<?,?> map) {
			var stack = new ItemStack(as_item(map.get("item")));
			for (var entry : ((Map<?,?>) map.get("tag")).entrySet())
				stack.addTagElement((String) entry.getKey(), to_nbt(entry.getValue()));
			return stack;
		}
		throw new IllegalArgumentException(o.getClass().toString());
	}
	public static <T> List<T> as_list(Object o, Function<Object, T> function) {
		return ((List<?>) o).stream().map(function).toList();
	}
	public static <T, T2> List<T2> map(List<T> list, Function<T, T2> function) {
		return list.stream().map(function).toList();
	}
	public static <K, V> Map<K, V> as_map(Object o, Function<Object, K> key_function,
	Function<Object, V> value_function) {
		if (o instanceof UnmodifiableConfig config) return as_map(config.valueMap(), key_function, value_function);
		return ((Map<?, ?>) o).entrySet().stream()
			.collect(HashMap::new,
				(map, entry) -> map.put(key_function.apply(entry.getKey()), value_function.apply(entry.getValue())),
				HashMap::putAll
			);
	}
	public static <K, V, K2, V2> Map<K2, V2> map(Map<K, V> map, Function<K, K2> key_function,
	Function<V, V2> value_function) {
		return map.entrySet().stream()
			.collect(HashMap::new,
				(map2, entry) -> map2.put(key_function.apply(entry.getKey()), value_function.apply(entry.getValue())),
				HashMap::putAll
			);
	}
	public static <K2, V2> Map<K2, V2> map(UnmodifiableConfig config, Function<String, K2> key_function,
	Function<Object, V2> value_function) {
		return map(config.valueMap(), key_function, value_function);
	}
	public static Tag to_nbt(Object o) {
		if (o instanceof Boolean bool) return ByteTag.valueOf(bool);
		else if (o instanceof String string) return StringTag.valueOf(string);
		else if (o instanceof Number num) return IntTag.valueOf(num.intValue());
		else if (o instanceof List<?> list) {
			var tag = new ListTag();
			tag.addAll(list.stream().map(Parsing::to_nbt).toList());
			return tag;
		} else if (o instanceof Map<?,?> map) {
			if (map.containsKey("tag_number"))
				if (validate_key(map, "byte", v -> v instanceof Number))
					return ByteTag.valueOf(((Number) map.get("byte")).byteValue());
				else if (validate_key(map, "double", v -> v instanceof Number))
					return DoubleTag.valueOf(((Number) map.get("double")).doubleValue());
				else if (validate_key(map, "float", v -> v instanceof Number))
					return FloatTag.valueOf(((Number) map.get("float")).floatValue());
				else if (validate_key(map, "long", v -> v instanceof Number))
					return LongTag.valueOf(((Number) map.get("long")).longValue());
				else if (validate_key(map, "short", v -> v instanceof Number))
					return ShortTag.valueOf(((Number) map.get("short")).shortValue());
			var tag = new CompoundTag();
			for (var entry : map.entrySet())
				tag.put((String) entry.getKey(), to_nbt(entry.getValue()));
			return tag;
		}
		throw new IllegalArgumentException(o.getClass().toString());
	}
	public static JsonElement tag_to_json(Tag tag) {
		if (tag instanceof NumericTag number_tag) {
			Number number = number_tag.getAsNumber();
			var type = "";
			if (number_tag instanceof ByteTag) { type = "byte"; number = number.byteValue(); }
			else if (number_tag instanceof DoubleTag) { type = "double"; number = number.doubleValue(); }
			else if (number_tag instanceof FloatTag) { type = "float"; number = number.floatValue(); }
			else if (number_tag instanceof LongTag) { type = "long"; number = number.longValue(); }
			else if (number_tag instanceof ShortTag) { type = "short"; number = number.shortValue(); }
			else if (number_tag instanceof IntTag) return new JsonPrimitive(number_tag.getAsInt());
			else throw new IllegalArgumentException("Could not convert NumericTag %s".formatted(number_tag.getClass()));
			var object = new JsonObject();
			object.addProperty("tag_number", type);
			object.addProperty(type, number);
			return object;
		} else if (tag instanceof StringTag string_tag) return new JsonPrimitive(string_tag.getAsString());
		else if (tag instanceof CollectionTag<?> collection_tag) {
			var array = new JsonArray();
			for (var tag2 : collection_tag)
				array.add(tag_to_json(tag2));
			return array;
		} else if (tag instanceof CompoundTag compound_tag) {
			var object = new JsonObject();
			for (var key : compound_tag.getAllKeys())
				object.add(key, tag_to_json(requireNonNull(compound_tag.get(key))));
			return object;
		}
		throw new IllegalArgumentException(tag.getClass().toString());
	}
}
