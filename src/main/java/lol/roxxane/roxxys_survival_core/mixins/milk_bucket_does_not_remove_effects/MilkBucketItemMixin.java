package lol.roxxane.roxxys_survival_core.mixins.milk_bucket_does_not_remove_effects;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
abstract class MilkBucketItemMixin {
	@Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
	private void ee(ItemStack stack, Level $1, LivingEntity $3, CallbackInfoReturnable<ItemStack> cir) {
		cir.setReturnValue(stack);
	}
}
