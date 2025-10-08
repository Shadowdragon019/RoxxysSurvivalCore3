package lol.roxxane.roxxys_survival_core.mixins.always_best_compost_chance;

import net.minecraft.world.level.block.ComposterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {
	@ModifyArg(method = "add", index = 1,
		at = @At(value = "INVOKE", remap = false,
			target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;put(Ljava/lang/Object;F)F"))
	private static float alwaysSucceed(float originalChance) {
		return 1;
	}
}
