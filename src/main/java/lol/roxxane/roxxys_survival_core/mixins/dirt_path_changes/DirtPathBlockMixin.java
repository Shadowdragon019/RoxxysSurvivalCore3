package lol.roxxane.roxxys_survival_core.mixins.dirt_path_changes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirtPathBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("deprecation")
@Mixin(DirtPathBlock.class)
abstract class DirtPathBlockMixin extends Block {
	public DirtPathBlockMixin(Properties $) { super($); }
	@Inject(method = "canSurvive", cancellable = true, at = @At("HEAD"))
	private void pathAlwaysSurvives(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(super.canSurvive(state, level, pos));
	}
}
