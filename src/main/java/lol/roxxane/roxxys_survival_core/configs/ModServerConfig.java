package lol.roxxane.roxxys_survival_core.configs;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.util.Id;
import lol.roxxane.roxxys_survival_core.util.New;
import lol.roxxane.roxxys_survival_core.util.Parsing;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static lol.roxxane.roxxys_survival_core.util.New.config;
import static lol.roxxane.roxxys_survival_core.util.Parsing.*;
import static lol.roxxane.roxxys_survival_core.util.fuck_off_exceptions.FuckOffExceptions.doesRun;
import static lol.roxxane.roxxys_survival_core.util.fuck_off_exceptions.FuckOffExceptions.trylog;
import static net.minecraftforge.fml.loading.FMLPaths.GAMEDIR;

@SuppressWarnings({"SameParameterValue", "unused"})
@EventBusSubscriber(modid = Rsc.ID, bus = EventBusSubscriber.Bus.MOD)
public class ModServerConfig {
	private static final Builder BUILDER = new Builder();
	private static final IntValue _SURVIVAL_MINING_COOLDOWN =
		_int("survival_mining_cooldown", 0, 0, "Vanilla value is 5");
	private static final IntValue _CREATIVE_MINING_COOLDOWN =
		_int("creative_mining_cooldown", 0, 0, "Vanilla value is 5");
	private static final BooleanValue _OVERRIDE_IFRAME_FUNCTIONALITY = _true("override_iframe_functionality");
	private static final IntValue _DEFAULT_IFRAMES = _int("default_iframes", 0, 0);
	private static final ConfigValue<UnmodifiableConfig> _DAMAGE_TYPE_IFRAMES =
		map_entries("damage_type_iframes", config("in_fire", 5, "lava", 5, "hot_floor", 5, "in_wall", 5, "cramming", 5,
				"cactus", 5, "out_of_world", 5, "dry_out", 5, "sweet_berry_bush", 5),
			Id::is,
			value -> value instanceof Integer iframes && iframes > 0);
	private static final BooleanValue _DISABLE_DURABILITY = _true("disable_durability");
	private static final DoubleValue _CONSISTENT_SLIME_DAMAGE = _double("slime_damage", 1, -1,
		"Makes damage consistent across slime sizes", "Set to -1 for vanilla behavior");
	@SuppressWarnings("ResultOfMethodCallIgnored")
	private static final ConfigValue<String> _RSC_RECIPES_COMMAND_OUTPUT =
		BUILDER.define("rsc_recipes_command_output", "rsc_output/recipes", o -> {
			if (o instanceof String string) {
				try {
					Path.of(GAMEDIR.get() + string);
					return true;
				} catch (Exception ignored) {}
			}
			return false;
		});
	private static final ConfigValue<Double> _CONSTANT_DESTROY_TIME = _double("constant_destroy_time", 0.25, -1);
	@SuppressWarnings("ResultOfMethodCallIgnored")
	private static final ConfigValue<UnmodifiableConfig> _BURNABLES = map_entries("burnables",
		config("1", New.list("dirt", "coarse_dirt"), "2", New.list("apple", "stick")),
		key -> doesRun(() -> Integer.parseInt(key)),
		value -> is_collection(value, Parsing::is_item));
	public static final ForgeConfigSpec SPEC = BUILDER.build();
	public static int survival_mining_cooldown = 0;
	public static int creative_mining_cooldown = 0;
	public static boolean override_iframe_functionality = true;
	public static int default_iframes = 0;
	public static final Map<ResourceLocation, Integer> DAMAGE_TYPE_IFRAMES = new HashMap<>();
	public static boolean disable_durability = true;
	public static double consistent_slime_damage = 1;
	public static Path rsc_recipes_command_output = Path.of(GAMEDIR.get() + "/rsc_output/recipes");
	public static double constant_destroy_time = 0.25f;
	public static final Map<Item, Integer> BURNABLES = new HashMap<>();
	@SubscribeEvent
	public static void on_reload(ModConfigEvent event) {
		// TODO: Figure out why this is being weird :/
		trylog(() -> survival_mining_cooldown = _SURVIVAL_MINING_COOLDOWN.get());
		trylog(() -> creative_mining_cooldown = _CREATIVE_MINING_COOLDOWN.get());
		trylog(() -> override_iframe_functionality = _OVERRIDE_IFRAME_FUNCTIONALITY.get());
		trylog(() -> default_iframes = _DEFAULT_IFRAMES.get());
		trylog(() -> {
			DAMAGE_TYPE_IFRAMES.clear();
			_DAMAGE_TYPE_IFRAMES.get().valueMap()
				.forEach((key, value) -> DAMAGE_TYPE_IFRAMES.put(Id.of(key), (int) value));
		});
		trylog(() -> disable_durability = _DISABLE_DURABILITY.get());
		trylog(() -> consistent_slime_damage = _CONSISTENT_SLIME_DAMAGE.get());
		trylog(() -> rsc_recipes_command_output = Path.of(GAMEDIR.get() + "/" + _RSC_RECIPES_COMMAND_OUTPUT.get()));
		trylog(() -> constant_destroy_time = _CONSTANT_DESTROY_TIME.get());
		trylog(() -> {
			BURNABLES.clear();
			_BURNABLES.get().valueMap().forEach((key, value) ->
				if_elements(value, element ->
					BURNABLES.put(as_item(element), Integer.parseInt(key))));
		});
	}
	// Bool
	private static BooleanValue bool(String name, boolean _default, String... comments) {
		return BUILDER.comment(comments).define(name, _default);
	}
	private static BooleanValue _true(String name, String... comments) {
		return bool(name, true, comments);
	}
	private static BooleanValue _false(String name, String... comments) {
		return bool(name, false, comments);
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
