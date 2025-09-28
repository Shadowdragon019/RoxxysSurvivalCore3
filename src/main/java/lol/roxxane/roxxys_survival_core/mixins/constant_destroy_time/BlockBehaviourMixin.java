package lol.roxxane.roxxys_survival_core.mixins.constant_destroy_time;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import lol.roxxane.roxxys_survival_core.configs.ModServerConfig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockBehaviour.BlockStateBase.class)
abstract class BlockBehaviourMixin {
	@Shadow public abstract Block getBlock();
	@ModifyReturnValue(method = "getDestroySpeed", at = @At("RETURN"))
	private float constantDestroyTime(float original) {
		if (ModServerConfig.constant_destroy_time < 0 || getBlock() == Blocks.FIRE)
			return original;
		return (float) ModServerConfig.constant_destroy_time;
	}
}
