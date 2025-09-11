package lol.roxxane.roxxys_survival_core.configs;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.util.Id;
import lol.roxxane.roxxys_survival_core.util.New;
import lol.roxxane.roxxys_survival_core.util.Parsing;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static lol.roxxane.roxxys_survival_core.util.New.config;
import static lol.roxxane.roxxys_survival_core.util.Parsing.*;

@SuppressWarnings({"SameParameterValue", "unused"})
@EventBusSubscriber(modid = Rsc.ID, bus = EventBusSubscriber.Bus.MOD)
public class ModClientConfig {
	private static final Builder BUILDER = new Builder();
	private static final ConfigValue<List<?>> _TABS_CLEAR =
		list_elements("tabs.clear", New.list("spawn_eggs"), Parsing::is_id);
	public static final ConfigValue<UnmodifiableConfig> _TABS_REMOVE_PRE =
		map_entries("tabs.remove.pre",
			config("tools_and_utilities", New.list("diamond_axe"), "combat", New.list("arrow")),
			Parsing::is_id,
			v -> is_collection(v, Parsing::is_stack));
	public static final ConfigValue<UnmodifiableConfig> _TABS_ADD_END =
		map_entries("tabs.add.end",
			config("tools_and_utilities", New.list("bedrock"), "combat", New.list("dirt")),
			Parsing::is_id,
			v -> is_collection(v, Parsing::is_stack));
	public static final ConfigValue<UnmodifiableConfig> _TABS_ADD_START =
		map_entries("tabs.add.start",
			config("tools_and_utilities", New.list("diamond"), "combat", New.list("gold_nugget")),
			Parsing::is_id,
			v -> is_collection(v, Parsing::is_stack));
	public static final ConfigValue<UnmodifiableConfig> _TABS_ADD_AFTER =
		map_entries("tabs.add.after",
			config("ingredients", config("diamond", New.list("dirt"), "iron_ingot", New.list("bedrock")), "building_blocks", config("oak_planks", New.list("iron_ingot"), "spruce_planks", New.list("stick"))),
			Parsing::is_id,
			v -> is_map(v, Parsing::is_stack, v2 -> is_collection(v2, Parsing::is_stack)));
	public static final ConfigValue<UnmodifiableConfig> _TABS_ADD_BEFORE =
		map_entries("tabs.add.before",
			config("ingredients", config("diamond", New.list("coarse_dirt"), "iron_ingot", New.list("diamond_block")), "building_blocks", config("oak_planks", New.list("gold_ingot"), "spruce_planks", New.list("diamond"))),
			Parsing::is_id,
			v -> is_map(v, Parsing::is_stack, v2 -> is_collection(v2, Parsing::is_stack)));
	public static final ConfigValue<UnmodifiableConfig> _TABS_REMOVE_POST =
		map_entries("tabs.remove.post",
			config("tools_and_utilities", New.list("diamond_pickaxe")),
			Parsing::is_id, v -> is_collection(v, Parsing::is_stack));
	public static final ForgeConfigSpec SPEC = BUILDER.build();
	public static final List<ResourceLocation> tabs_clear = new ArrayList<>();
	public static final Map<ResourceLocation, List<ItemStack>> tabs_remove_pre = new HashMap<>();
	public static final Map<ResourceLocation, List<ItemStack>> tabs_add_end = new HashMap<>();
	public static final Map<ResourceLocation, List<ItemStack>> tabs_add_start = new HashMap<>();
	public static final Map<ResourceLocation, Map<ItemStack, List<ItemStack>>> tabs_add_after = new HashMap<>();
	public static final Map<ResourceLocation, Map<ItemStack, List<ItemStack>>> tabs_add_before = new HashMap<>();
	public static final Map<ResourceLocation, List<ItemStack>> tabs_remove_post = new HashMap<>();
	@SubscribeEvent
	public static void on_reload(ModConfigEvent event) {
		tabs_clear.clear();
		tabs_clear.addAll(_TABS_CLEAR.get().stream().map(o -> Id.of((String) o)).toList());
		tabs_remove_pre.clear();
		tabs_remove_pre.putAll(Parsing.map(_TABS_REMOVE_PRE.get(), Id::of, v -> as_list(v, Parsing::as_stack)));
		tabs_add_end.clear();
		tabs_add_end.putAll(Parsing.map(_TABS_ADD_END.get(), Id::of, v -> as_list(v, Parsing::as_stack)));
		tabs_add_start.clear();
		tabs_add_start.putAll(Parsing.map(_TABS_ADD_START.get(), Id::of, v -> as_list(v, Parsing::as_stack)));
		tabs_add_after.clear();
		tabs_add_after.putAll(Parsing.map(_TABS_ADD_AFTER.get(), Id::of,
			v -> as_map(v, Parsing::as_stack, v2 -> as_list(v2, Parsing::as_stack))));
		tabs_add_before.clear();
		tabs_add_before.putAll(Parsing.map(_TABS_ADD_BEFORE.get(), Id::of,
			v -> as_map(v, Parsing::as_stack, v2 -> as_list(v2, Parsing::as_stack))));
		tabs_remove_post.clear();
		tabs_remove_post.putAll(Parsing.map(_TABS_REMOVE_POST.get(), Id::of, v -> as_list(v, Parsing::as_stack)));
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
