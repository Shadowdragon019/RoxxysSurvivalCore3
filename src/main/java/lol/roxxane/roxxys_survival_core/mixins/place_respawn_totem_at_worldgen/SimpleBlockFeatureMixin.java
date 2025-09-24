package lol.roxxane.roxxys_survival_core.mixins.place_respawn_totem_at_worldgen;

import com.llamalad7.mixinextras.sugar.Local;
import lol.roxxane.roxxys_survival_core.blocks.RespawnTotemBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static lol.roxxane.roxxys_survival_core.blocks.ModStateProperties.RESPAWN_TOTEM_PART;
import static lol.roxxane.roxxys_survival_core.blocks.state_property_enums.RespawnTotemPart.MIDDLE;
import static lol.roxxane.roxxys_survival_core.blocks.state_property_enums.RespawnTotemPart.TOP;

@Mixin(SimpleBlockFeature.class)
abstract class SimpleBlockFeatureMixin {
	@Inject(method = "place",
		cancellable = true,
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"))
	private void respawnTotemPlacer(FeaturePlaceContext<SimpleBlockConfiguration> context, CallbackInfoReturnable<Boolean> cir, @Local BlockState state, @Local WorldGenLevel level, @Local BlockPos pos) {
		if (state.getBlock() instanceof RespawnTotemBlock) {
			if (!rsc$canPlaceAt(level, pos.above()))
				cir.setReturnValue(false);
			if (!rsc$canPlaceAt(level, pos.above(2)))
				cir.setReturnValue(false);
			level.setBlock(pos.above(), state.setValue(RESPAWN_TOTEM_PART, MIDDLE), 2);
			level.setBlock(pos.above(2), state.setValue(RESPAWN_TOTEM_PART, TOP), 2);
		}
	}
	@Unique
	private boolean rsc$canPlaceAt(WorldGenLevel level, BlockPos pos) {
		return level.isEmptyBlock(pos) || level.getBlockState(pos).canBeReplaced();
	}
}
