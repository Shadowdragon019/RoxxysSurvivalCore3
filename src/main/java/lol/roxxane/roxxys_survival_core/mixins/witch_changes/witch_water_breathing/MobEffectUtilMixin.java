package lol.roxxane.roxxys_survival_core.mixins.witch_changes.witch_water_breathing;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static lol.roxxane.roxxys_survival_core.mob_effects.ModMobEffects.WITCH_WATER_BREATHING;

@Mixin(MobEffectUtil.class)
abstract class MobEffectUtilMixin {
	@ModifyReturnValue(method = "hasWaterBreathing", at = @At("RETURN"))
	private static boolean hasWaterBreathing(boolean original, @Local(argsOnly = true) LivingEntity living) {
		return original || living.hasEffect(WITCH_WATER_BREATHING.get());
	}
}
