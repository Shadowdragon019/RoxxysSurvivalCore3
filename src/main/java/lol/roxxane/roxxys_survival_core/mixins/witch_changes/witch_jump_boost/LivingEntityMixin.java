package lol.roxxane.roxxys_survival_core.mixins.witch_changes.witch_jump_boost;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;
import static lol.roxxane.roxxys_survival_core.mob_effects.ModMobEffects.WITCH_JUMP_BOOST;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {
	@Shadow @Nullable public abstract MobEffectInstance getEffect(MobEffect pEffect);
	@Shadow public abstract boolean hasEffect(MobEffect pEffect);
	@ModifyVariable(method = "calculateFallDamage",
		at = @At("STORE"), ordinal = 2)
	private float calculateFallDamage(float original) {
		var effect = getEffect(WITCH_JUMP_BOOST.get());
		return original + (effect == null ? 0 : effect.getAmplifier() + 1);
	}
	@ModifyReturnValue(method = "getJumpBoostPower", at = @At("RETURN"))
	private float getJumpBoostPower(float original) {
		var newValue = original;
		if (hasEffect(WITCH_JUMP_BOOST.get()))
			newValue += 0.1f * requireNonNull(getEffect(WITCH_JUMP_BOOST.get())).getAmplifier();
		return newValue;
	}
}
