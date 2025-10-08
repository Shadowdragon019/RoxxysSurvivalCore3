package lol.roxxane.roxxys_survival_core.configs;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import lol.roxxane.roxxys_survival_core.util.New;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static lol.roxxane.roxxys_survival_core.Rsc.*;
import static lol.roxxane.roxxys_survival_core.util.Parsing.*;
import static net.minecraftforge.fml.loading.FMLPaths.CONFIGDIR;

public class ModClientJsonConfig {
	public static boolean enable_tab_manipulation = true;
	public static boolean rsc_command_outputs_pretty_json = true;
	public static boolean item_tags_in_tooltip = false;
	public static boolean block_tags_in_tooltip = false;
	public static boolean tag_in_tooltip = false;
	public static boolean detailed_tag_in_tooltips = false;
	public static final Map<ResourceLocation, List<ItemStack>> TABS_REMOVE_PRE = new LinkedHashMap<>();
	public static final List<ResourceLocation> TABS_CLEAR = new ArrayList<>();
	public static final Map<ResourceLocation, List<ItemStack>> TABS_ADD_END = new LinkedHashMap<>();
	public static final Map<ResourceLocation, List<ItemStack>> TABS_ADD_START = new LinkedHashMap<>();
	public static final Map<ResourceLocation, Map<ItemStack, List<ItemStack>>> TABS_ADD_AFTER = new LinkedHashMap<>();
	public static final Map<ResourceLocation, Map<ItemStack, List<ItemStack>>> TABS_ADD_BEFORE = new LinkedHashMap<>();
	public static final Map<ResourceLocation, List<ItemStack>> TABS_REMOVE_POST = new LinkedHashMap<>();
	public static final List<ResourceLocation> REMOVE_JEI_CATEGORIES = new ArrayList<>();
	private static final Path PATH = Path.of(CONFIGDIR.get().toString() + "/roxxys_survival_core_client.json");
	public static void load() {
		if (!Files.exists(PATH))
			try {
				make_default_file();
			} catch (Exception e) {
				log("Failed to create default file for {}", PATH);
				throw new RuntimeException(e);
			}
		try {
			read_file();
		} catch (FileNotFoundException e) {
			log("Failed to read file for {}", PATH);
			throw new RuntimeException(e);
		}
	}
	private static void make_default_file() throws IOException {
		var data = PRETTY_GSON.toJsonTree(New.map(
			"enable_tab_manipulation", true,
			"tabs", New.map(
				"add_end", New.map(
					"ingredients", New.list(
						"dirt"
					)
				),
				"clear", New.list(
					"spawn_eggs"
				),
				"remove_post", New.map(
					"tools_and_utilities", New.list(
						"diamond_axe"
					),
					"combat", New.list(
						New.map(
							"item", "tipped_arrow",
							"tag", New.map(
								"Potion", "minecraft:water"
							)
						)
					),
					"ingredients", New.list(
						New.map(
							"item", "enchanted_book",
							"tag", New.map(
								"StoredEnchantments", New.list(
									New.map(
										"id", "minecraft:fire_protection",
										"lvl", New.map(
											"tag_number", "short",
											"short", 4
										)
									)
								)
							)
						)
					)
				),
				"add_start", New.map(
					"combat", New.list(
						"diamond_block"
					)
				),
				"add_after", New.map(
					"natural_blocks", New.map(
						"dirt", New.list(
							"iron_ingot"
						)
					)
				),
				"add_before", New.map(
					"natural_blocks", New.map(
						"dirt", New.list(
							"gold_ingot"
						)
					)
				)
			),
			"item_tags_in_tooltip", true,
			"block_tags_in_tooltip", true,
			"tag_in_tooltip", true,
			"detailed_tag_in_tooltips", false,
			"rsc_command_outputs_pretty_json", true,
			"remove_jei_categories", New.list(
				"anvil", "brewing"
			),
			"burnables", New.map(
				"coal", 0
			)
		));
		var writer = new FileWriter(PATH.toString());
		writer.write(PRETTY_GSON.toJson(data));
		writer.close();
	}
	private static void read_file() throws FileNotFoundException {
		var reader = new JsonReader(new FileReader(PATH.toString()));
		@SuppressWarnings("unchecked") var data =
			(Map<String, Object>) json_to_normal(PRETTY_GSON.fromJson(reader, JsonObject.class));
		TABS_CLEAR.clear();
		TABS_REMOVE_PRE.clear();
		TABS_ADD_END.clear();
		TABS_ADD_START.clear();
		TABS_ADD_AFTER.clear();
		TABS_ADD_BEFORE.clear();
		TABS_REMOVE_POST.clear();
		if_has_bool(data, "item_tags_in_tooltip", bool -> item_tags_in_tooltip = bool);
		if_has_bool(data, "block_tags_in_tooltip", bool -> block_tags_in_tooltip = bool);
		if_has_bool(data, "tag_in_tooltip", bool -> tag_in_tooltip = bool);
		if_has_bool(data, "detailed_tag_in_tooltips", bool -> detailed_tag_in_tooltips = bool);
		if_has_bool(data, "rsc_command_outputs_pretty_json", bool -> rsc_command_outputs_pretty_json = bool);
		if_has_bool(data, "enable_tab_manipulation", bool -> enable_tab_manipulation = bool);
		if_has_map(data, "tabs", tab_data -> {
			if_has_elements(tab_data, "clear", tab -> if_id(tab, TABS_CLEAR::add));
			if_has_entries(tab_data, "remove_pre", (tab, items_list) -> {
				if (is_id(tab)) {
					if_list(items_list, items_list2 -> {
						for (var item : items_list2) {
							if_stack(item, stack ->
								TABS_REMOVE_PRE.computeIfAbsent(
									as_id(tab),
									key -> new ArrayList<>()
								).add(stack));
						}
					});
				}
			});
			if_has_entries(tab_data, "add_end", (tab, items_list) -> {
				if (is_id(tab))
					if_list(items_list, items_list2 -> {
						for (var item : items_list2)
							if_stack(item, stack ->
								TABS_ADD_END.computeIfAbsent(
									as_id(tab),
									key -> new ArrayList<>()
								).add(stack));
					});
			});
			if_has_map(tab_data, "add_start", tabs -> {
				for (var entry : tabs.entrySet()) {
					var tab = entry.getKey();
					var items_list = entry.getValue();
					if (is_id(tab))
						if_list(items_list, items_list2 -> {
							for (var item : items_list2)
								if_stack(item, stack ->
									TABS_ADD_START.computeIfAbsent(
										as_id(tab),
										key -> new ArrayList<>()
									).add(stack));
						});
				}
			});
			if_has_map(tab_data, "add_after", tabs -> {
				for (var entry : tabs.entrySet()) {
					var tab_id_string = entry.getKey();
					var tab_item_data = entry.getValue();
					if (is_id(tab_id_string))
						if_entries(tab_item_data, (after, items) -> {
							if (is_item(after))
								if_elements(items, item_element ->
									if_stack(item_element, stack ->
										TABS_ADD_AFTER.computeIfAbsent(
											as_id(tab_id_string),
											$ -> new LinkedHashMap<>()
										).computeIfAbsent(as_stack(after),
											$ -> new ArrayList<>()
										).add(stack)));
						});
				}
			});
			if_has_map(tab_data, "add_before", tabs -> {
				for (var entry : tabs.entrySet()) {
					var tab_id_string = entry.getKey();
					var tab_item_data = entry.getValue();
					if (is_id(tab_id_string))
						if_entries(tab_item_data, (after, items) -> {
							if (is_item(after))
								if_elements(items, item_element ->
									if_stack(item_element, stack ->
										TABS_ADD_BEFORE.computeIfAbsent(
											as_id(tab_id_string),
											$ -> new LinkedHashMap<>()
										).computeIfAbsent(as_stack(after),
											$ -> new ArrayList<>()
										).add(stack)));
						});
				}
			});
			if_has_entries(tab_data, "remove_post", (tab, items_list) -> {
				if (is_id(tab)) {
					if_list(items_list, items_list2 -> {
						for (var item : items_list2) {
							if_stack(item, stack ->
								TABS_REMOVE_POST.computeIfAbsent(
									as_id(tab),
									key -> new ArrayList<>()
							).add(stack));
						}
					});
				}
			});
		});
		REMOVE_JEI_CATEGORIES.clear();
		if_has_elements(data, "remove_jei_categories", element ->
			if_id(element, REMOVE_JEI_CATEGORIES::add));
	}
	public static Gson get_command_gson() {
		return rsc_command_outputs_pretty_json ? PRETTY_GSON: COMPACT_GSON;
	}
}
