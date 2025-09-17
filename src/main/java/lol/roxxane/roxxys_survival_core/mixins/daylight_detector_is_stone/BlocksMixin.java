package lol.roxxane.roxxys_survival_core.mixins.daylight_detector_is_stone;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
abstract class BlocksMixin {
	@ModifyExpressionValue(method = "<clinit>",
		slice = @Slice(
			from = @At(value = "FIELD",
				target = "Lnet/minecraft/world/level/block/Blocks;COMPARATOR:Lnet/minecraft/world/level/block/Block;"),
			to = @At(value = "FIELD",
				target = "Lnet/minecraft/world/level/block/Blocks;DAYLIGHT_DETECTOR:Lnet/minecraft/world/level/block/Block;")
		),
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;ignitedByLava()Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;"))
	private static Properties redirect_properties(Properties original) {
		return original.mapColor(MapColor.STONE).sound(SoundType.STONE).requiresCorrectToolForDrops();
	}
}
