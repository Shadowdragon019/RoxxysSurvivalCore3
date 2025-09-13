package lol.roxxane.roxxys_survival_core.configs;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import lol.roxxane.roxxys_survival_core.Rsc;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@SuppressWarnings({"SameParameterValue", "unused"})
@EventBusSubscriber(modid = Rsc.ID, bus = EventBusSubscriber.Bus.MOD)
public class ModClientConfig {
	private static final Builder BUILDER = new Builder();
	public static final ForgeConfigSpec SPEC = BUILDER.build();
	@SubscribeEvent
	public static void on_reload(ModConfigEvent event) {

	}
	// Bool
	private static BooleanValue bool(String name, boolean _default, String... comments) {
		return BUILDER.comment(comments).define(name, _default);
	}
	// Int
	private static IntValue _int(String name, int _default, int min, int max, String... comments) {
		return BUILDER.comment(comments).defineInRange(name, _default, min, max);
	}
	private static IntValue _int(String name, int _default, int min, String... comments) {
		return _int(name, _default, min, Integer.MAX_VALUE, comments);
	}
	// Double
	private static DoubleValue _double(String name, double _default, double min, double max, String... comments) {
		return BUILDER.comment(comments).defineInRange(name, _default, min, max);
	}
	private static DoubleValue _double(String name, double _default, double min, String... comments) {
		return _double(name, _default, min, Double.MAX_VALUE, comments);
	}
	// List
	private static ConfigValue<List<?>> list(String name, List<?> _default, Predicate<List<?>> predicate,
	String... comments) {
		return BUILDER.comment(comments).define(name, _default,
			object -> object instanceof List<?> list && predicate.test(list));
	}
	private static ConfigValue<List<?>> list_elements(String name, List<?> _default, Predicate<Object> predicate,
	String... comments) {
		return BUILDER.comment(comments).define(name, _default,
			object -> object instanceof List<?> list && list.stream().allMatch(predicate));
	}
	// Map
	private static ConfigValue<UnmodifiableConfig> map(String name, UnmodifiableConfig _default,
	Predicate<Map<String, Object>> predicate, String... comments) {
		return BUILDER.comment(comments).define(name, _default,
			object -> object instanceof UnmodifiableConfig config && predicate.test(config.valueMap()));
	}
	private static ConfigValue<UnmodifiableConfig> map_entries(String name, UnmodifiableConfig _default,
	BiPredicate<String, Object> predicate, String... comments) {
		return map(name, _default, map ->
			map.entrySet().stream().allMatch(entry -> predicate.test(entry.getKey(), entry.getValue())), comments);
	}
	private static ConfigValue<UnmodifiableConfig> map_entries(String name, UnmodifiableConfig _default,
	Predicate<String> key_predicate, Predicate<Object> value_predicate, String... comments) {
		return map(name, _default, map ->
			map.keySet().stream().allMatch(key_predicate) &&
				map.values().stream().allMatch(value_predicate), comments);
	}
}
