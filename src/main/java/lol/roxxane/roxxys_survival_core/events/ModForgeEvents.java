package lol.roxxane.roxxys_survival_core.events;

import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.commands.RscCommand;
import lol.roxxane.roxxys_survival_core.configs.ModClientJsonConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import static lol.roxxane.roxxys_survival_core.configs.ModServerConfig.*;

@EventBusSubscriber(modid = Rsc.ID, bus = EventBusSubscriber.Bus.FORGE)
public class ModForgeEvents {
	@SubscribeEvent
	public static void living_hurt(LivingDamageEvent event) {
		var entity = event.getEntity();
		@SuppressWarnings("resource") var level = entity.level();
		if (override_iframe_functionality) {
			var type = event.getSource().type();
			var registry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
			entity.invulnerableTime = DAMAGE_TYPE_IFRAMES.getOrDefault(registry.getKey(type), default_iframes);
		}
	}
	@SubscribeEvent
	public static void register_commands(RegisterCommandsEvent event) {
		var dispatcher = event.getDispatcher();
		//RscReloadCommand.register(dispatcher);
		RscCommand.register(dispatcher, event.getBuildContext());
	}
	@SubscribeEvent
	public static void tooltip_event(ItemTooltipEvent event) {
		var tooltip = event.getToolTip();
		var stack = event.getItemStack();
		var item = stack.getItem();
		var item_tag_list = stack.getTags().map(tag ->
			Component.literal("§2 " + tag.location())).toList();
		if (Minecraft.getInstance().options.advancedItemTooltips) {
			if (ModClientJsonConfig.item_tags_in_tooltip && !item_tag_list.isEmpty()) {
				tooltip.add(Component.empty());
				tooltip.add(Component.translatable("tooltip.roxxys_survival_core.item_tags_header"));
				tooltip.addAll(item_tag_list);
			}
			if (item instanceof BlockItem block_item && ModClientJsonConfig.block_tags_in_tooltip) {
				var block = block_item.getBlock();
				@SuppressWarnings("deprecation")
				var block_tag_list = block.builtInRegistryHolder().tags()
					.map(tag -> Component.literal("§2 " + tag.location())).toList();
				if (!block_tag_list.isEmpty()) {
					tooltip.add(Component.empty());
					tooltip.add(Component.translatable("tooltip.roxxys_survival_core.block_tags_header"));
					tooltip.addAll(block_tag_list);
				}
			}
			if (ModClientJsonConfig.tag_in_tooltip) {
				var tag = stack.serializeNBT();
				if (!ModClientJsonConfig.detailed_tag_in_tooltips)
					tag = tag.contains("tag") ? tag.getCompound("tag") : new CompoundTag();
				if (!tag.isEmpty()) {
					tooltip.add(Component.empty());
					tooltip.add(Component.translatable("tooltip.roxxys_survival_core.tag_header"));
					tooltip.add(Component.literal("§2 " + tag));
				}
			}
		}
	}
	@SubscribeEvent
	public static void reload(AddReloadListenerEvent event) {
		ModClientJsonConfig.load();
	}
}
