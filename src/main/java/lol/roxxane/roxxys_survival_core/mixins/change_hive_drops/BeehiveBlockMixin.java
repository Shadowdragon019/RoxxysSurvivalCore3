package lol.roxxane.roxxys_survival_core.mixins.change_hive_drops;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeehiveBlock.class)
abstract class BeehiveBlockMixin {
	@ModifyExpressionValue(method = "use",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
	private boolean redirect_is_glass_bottle_check(boolean original) {
		return false;
	}
	@Inject(method = "use",
		at = @At(value = "INVOKE",
			remap = false,
			target = "Lnet/minecraft/world/item/ItemStack;canPerformAction(Lnet/minecraftforge/common/ToolAction;)Z"))
	private void customDrops(BlockState $, Level level, BlockPos pos, Player $1, InteractionHand $2, BlockHitResult $3, CallbackInfoReturnable<InteractionResult> $4, @Local LocalBooleanRef flag) {
		Block.popResource(level, pos, new ItemStack(Items.HONEYCOMB_BLOCK));
		flag.set(true);
	}
}
