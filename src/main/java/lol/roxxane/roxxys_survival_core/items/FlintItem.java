package lol.roxxane.roxxys_survival_core.items;

import lol.roxxane.roxxys_survival_core.blocks.ModBlocks;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;

public class FlintItem extends BlockItem {
	public FlintItem(Properties properties) {
		super(ModBlocks.FLINT.get(), properties);
	}
	public InteractionResult useOn(UseOnContext context) {
		var player = context.getPlayer();
		if (player == null) return super.useOn(context);
		var offhandItem = player.getOffhandItem();
		var mainHandItem = player.getMainHandItem();
		if (!offhandItem.is(ModItems.FLINT.get())) return super.useOn(context);
		if (!mainHandItem.is(ModItems.FLINT.get())) return super.useOn(context);
		return Items.FLINT_AND_STEEL.useOn(context);
	}
}
