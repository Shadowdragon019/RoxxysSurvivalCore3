package lol.roxxane.roxxys_survival_core.events;

import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.configs.ModClientJsonConfig;
import lol.roxxane.roxxys_survival_core.items.ModItems;
import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.ArrayList;

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
		if (current_tab.equals(INGREDIENTS) || is_search)
			entries.putAfter(Items.FLINT.getDefaultInstance(), ModItems.FLINT.get().getDefaultInstance(),
				PARENT_AND_SEARCH_TABS);
		for (var tab : ModClientJsonConfig.TABS_CLEAR)
			if (tab.equals(current_tab.location())) {
				var stacks = new ArrayList<ItemStack>();
				for (var entry : event.getEntries())
					stacks.add(entry.getKey());
				for (var stack : stacks)
					event.getEntries().remove(stack);
			}
		for (var entry : ModClientJsonConfig.TABS_REMOVE_PRE.entrySet()) {
			var tab = entry.getKey();
			var stacks = entry.getValue();
			if (tab.equals(current_tab.location()) || is_search || tab.equals(Id.of("all")))
				for (var stack : stacks)
					event.getEntries().remove(stack);
		}
		for (var entry : ModClientJsonConfig.TABS_ADD_END.entrySet()) {
			var tab = entry.getKey();
			var stacks = entry.getValue();
			if (tab.equals(current_tab.location()) || is_search || tab.equals(Id.of("all")))
				for (var stack : stacks)
					event.accept(stack);
		}
		for (var entry : ModClientJsonConfig.TABS_ADD_START.entrySet()) {
			var tab = entry.getKey();
			var stacks = entry.getValue();
			if (tab.equals(current_tab.location()) || is_search || tab.equals(Id.of("all")))
				for (var stack : stacks)
					event.getEntries().putFirst(stack, PARENT_AND_SEARCH_TABS);
		}
		for (var entry : ModClientJsonConfig.TABS_ADD_AFTER.entrySet()) {
			var tab = entry.getKey();
			var item_map = entry.getValue();
			if (tab.equals(current_tab.location()) || is_search || tab.equals(Id.of("all")))
				for (var entry2 : item_map.entrySet()) {
					var after = entry2.getKey();
					var stacks = entry2.getValue();
					for (var stack : stacks)
						event.getEntries().putAfter(after, stack, PARENT_AND_SEARCH_TABS);
				}
		}
		for (var entry : ModClientJsonConfig.TABS_ADD_BEFORE.entrySet()) {
			var tab = entry.getKey();
			var item_map = entry.getValue();
			if (tab.equals(current_tab.location()) || is_search || tab.equals(Id.of("all")))
				for (var entry2 : item_map.entrySet()) {
					var before = entry2.getKey();
					var stacks = entry2.getValue();
					for (var stack : stacks)
						event.getEntries().putBefore(before, stack, PARENT_AND_SEARCH_TABS);
				}
		}
		for (var entry : ModClientJsonConfig.TABS_REMOVE_POST.entrySet()) {
			var tab = entry.getKey();
			var stacks = entry.getValue();
			if (tab.equals(current_tab.location()) || is_search || tab.equals(Id.of("all")))
				for (var stack : stacks)
					event.getEntries().remove(stack);
		}
	}
}
