package lol.roxxane.roxxys_survival_core.mixins.kswitch;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.kiwi.customization.block.family.BlockFamilies;

@Mixin(value = BlockFamilies.class, remap = false)
abstract class ConsistentConvertRatio {
	@Inject(method = "getConvertRatio", cancellable = true, at = @At("HEAD"))
	private static void consistentConvertRatio(Item item, CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(1f);
	}
}
