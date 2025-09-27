package lol.roxxane.roxxys_survival_core.mixins.pickaxes_act_as_axe_and_shovels;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PickaxeItem.class)
abstract class PickaxeItemMixin extends DiggerItem {
	public PickaxeItemMixin(float $, float $1, Tier $2, TagKey<Block> $3, Properties $4) { super($, $1, $2, $3, $4); }
	@ModifyReturnValue(method = "canPerformAction", remap = false, at = @At("RETURN"))
	private boolean canPerformAction(boolean original, @Local(argsOnly = true) ToolAction action) {
		return original ||
			ToolActions.DEFAULT_AXE_ACTIONS.contains(action) ||
			ToolActions.DEFAULT_SHOVEL_ACTIONS.contains(action);
	}
	public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
		var axeResult = Items.WOODEN_AXE.useOn(context);
		if (axeResult == InteractionResult.PASS)
			return Items.WOODEN_SHOVEL.useOn(context);
		return axeResult;
	}
}
