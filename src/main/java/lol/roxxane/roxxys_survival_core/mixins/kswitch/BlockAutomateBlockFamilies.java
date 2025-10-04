package lol.roxxane.roxxys_survival_core.mixins.kswitch;

import org.spongepowered.asm.mixin.Mixin;
import snownee.kiwi.customization.block.family.BlockFamilyInferrer;

@Mixin(value = BlockFamilyInferrer.class, remap = false)
abstract class BlockAutomateBlockFamilies {
	/*@Inject(method = "generate", cancellable = true,
		at = @At("HEAD"))
	private void block(CallbackInfoReturnable<Collection<KHolder<BlockFamily>>> cir) {
		cir.setReturnValue(List.of());
	}*/
}
