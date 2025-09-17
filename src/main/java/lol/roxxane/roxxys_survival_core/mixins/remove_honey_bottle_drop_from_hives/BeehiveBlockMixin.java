package lol.roxxane.roxxys_survival_core.mixins.remove_honey_bottle_drop_from_hives;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.block.BeehiveBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BeehiveBlock.class)
abstract class BeehiveBlockMixin {
	@ModifyExpressionValue(method = "use",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
	private boolean redirect_is_glass_bottle_check(boolean original) {
		return false;
	}
}
