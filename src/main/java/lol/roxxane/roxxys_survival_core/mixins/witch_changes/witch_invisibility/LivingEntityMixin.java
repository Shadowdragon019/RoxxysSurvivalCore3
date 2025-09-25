package lol.roxxane.roxxys_survival_core.mixins.witch_changes.witch_invisibility;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import lol.roxxane.roxxys_survival_core.mob_effects.ModMobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {
	@Shadow public abstract boolean hasEffect(MobEffect pEffect);
	@ModifyExpressionValue(method = "updateInvisibilityStatus",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z"))
	private boolean updateInvisibilityStatus(boolean original) {
		return original || hasEffect(ModMobEffects.WITCH_INVISIBILITY.get());
	}
}
