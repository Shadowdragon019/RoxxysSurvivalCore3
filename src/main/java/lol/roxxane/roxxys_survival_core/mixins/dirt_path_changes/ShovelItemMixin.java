package lol.roxxane.roxxys_survival_core.mixins.dirt_path_changes;

import com.google.common.collect.ImmutableMap.Builder;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShovelItem.class)
abstract class ShovelItemMixin {
	@ModifyExpressionValue(method = "useOn",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;isEmptyBlock(Lnet/minecraft/core/BlockPos;)Z"))
	private boolean isAirRedirect(boolean original) {
		return true;
	}
	@ModifyExpressionValue(method = "<clinit>",
		at = @At(value = "INVOKE", remap = false,
			ordinal = 0,
			target = "Lcom/google/common/collect/ImmutableMap$Builder;put(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;"))
	private static Builder<Block, BlockState> pathToDirt(Builder<Block, BlockState> original) {
		return original.put(Blocks.DIRT_PATH, Blocks.DIRT.defaultBlockState());
	}
}
