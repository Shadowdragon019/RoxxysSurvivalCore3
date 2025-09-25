package lol.roxxane.roxxys_survival_core.mixins.witch_changes.witch_fire_resistance;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static lol.roxxane.roxxys_survival_core.mob_effects.ModMobEffects.WITCH_FIRE_RESISTANCE;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {
	@Shadow public abstract boolean hasEffect(MobEffect pEffect);
	@ModifyExpressionValue(method = "hurt",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z"))
	private boolean hurt(boolean original) {
		return original || hasEffect(WITCH_FIRE_RESISTANCE.get());
	}
}
