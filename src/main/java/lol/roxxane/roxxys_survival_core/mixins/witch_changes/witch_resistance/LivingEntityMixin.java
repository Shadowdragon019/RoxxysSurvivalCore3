package lol.roxxane.roxxys_survival_core.mixins.witch_changes.witch_resistance;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

import static lol.roxxane.roxxys_survival_core.mob_effects.ModMobEffects.WITCH_RESISTANCE;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {
	@Shadow public abstract boolean hasEffect(MobEffect pEffect);
	@Shadow @Nullable public abstract MobEffectInstance getEffect(MobEffect pEffect);
	@ModifyExpressionValue(method = "getDamageAfterMagicAbsorb",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z"))
	private boolean checkForWitchResistance(boolean original) {
		return original || hasEffect(WITCH_RESISTANCE.get());
	}
	@ModifyExpressionValue(method = "getDamageAfterMagicAbsorb",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/effect/MobEffectInstance;getAmplifier()I"))
	private int getWitchResistanceAmplifier(int original) {
		var witchResistance = getEffect(WITCH_RESISTANCE.get());
		if (witchResistance == null)
			return original;
		return original + witchResistance.getAmplifier();
	}
	@ModifyExpressionValue(method = "getDamageAfterMagicAbsorb",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;getEffect(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/world/effect/MobEffectInstance;"))
	private MobEffectInstance preventNpe(MobEffectInstance original) {
		if (original == null)
			return new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE);
		return original;
	}
}
