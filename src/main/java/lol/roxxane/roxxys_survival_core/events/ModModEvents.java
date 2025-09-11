package lol.roxxane.roxxys_survival_core.events;

import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.items.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.ArrayList;

import static lol.roxxane.roxxys_survival_core.configs.ModClientConfig.*;
import static net.minecraft.world.item.CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
import static net.minecraft.world.item.CreativeModeTabs.INGREDIENTS;
import static net.minecraft.world.item.CreativeModeTabs.SEARCH;

@EventBusSubscriber(modid = Rsc.ID, bus = EventBusSubscriber.Bus.MOD)
public class ModModEvents {
	@SubscribeEvent
	public static void build_tabs(BuildCreativeModeTabContentsEvent event) {
		var current_tab = event.getTabKey();
		var entries = event.getEntries();
		var is_search = current_tab.equals(SEARCH);
		if (current_tab.equals(INGREDIENTS) || is_search) {
			entries.putAfter(Items.FLINT.getDefaultInstance(), ModItems.FLINT.get().getDefaultInstance(),
				PARENT_AND_SEARCH_TABS);
		}
		for (var clear_tab : tabs_clear)
			if (clear_tab.equals(current_tab.location())) {
				var stacks = new ArrayList<ItemStack>();
				for (var entry : event.getEntries())
					stacks.add(entry.getKey());
				for (var stack : stacks)
					entries.remove(stack);
			}
		for (var entry : tabs_remove_pre.entrySet()) {
			var tab = entry.getKey();
			var stacks = entry.getValue();
			if (tab.equals(current_tab.location()) || is_search)
				for (var stack : stacks)
					entries.remove(stack);
		}
		for (var entry : tabs_add_end.entrySet()) {
			var tab = entry.getKey();
			var stacks = entry.getValue();
			if (tab.equals(current_tab.location()) || is_search)
				for (var stack : stacks)
					event.accept(stack);
		}
		for (var entry : tabs_add_start.entrySet()) {
			var tab = entry.getKey();
			var stacks = entry.getValue();
			if (tab.equals(current_tab.location()) || is_search)
				for (var stack : stacks)
					entries.putFirst(stack, PARENT_AND_SEARCH_TABS);
		}
		for (var entry : tabs_add_after.entrySet()) {
			var tab = entry.getKey();
			var item_map = entry.getValue();
			if (tab.equals(current_tab.location()) || is_search)
				for (var entry2 : item_map.entrySet()) {
					var after = entry2.getKey();
					var stacks = entry2.getValue();
					for (var stack : stacks)
						entries.putAfter(after, stack, PARENT_AND_SEARCH_TABS);
				}
		}
		for (var entry : tabs_add_before.entrySet()) {
			var tab = entry.getKey();
			var item_map = entry.getValue();
			if (tab.equals(current_tab.location()) || is_search)
				for (var entry2 : item_map.entrySet()) {
					var before = entry2.getKey();
					var stacks = entry2.getValue();
					for (var stack : stacks)
						entries.putBefore(before, stack, PARENT_AND_SEARCH_TABS);
				}
		}
		for (var entry : tabs_remove_post.entrySet()) {
			var tab = entry.getKey();
			var stacks = entry.getValue();
			if (tab.equals(current_tab.location()) || is_search)
				for (var stack : stacks)
					entries.remove(stack);
		}
	}
}
