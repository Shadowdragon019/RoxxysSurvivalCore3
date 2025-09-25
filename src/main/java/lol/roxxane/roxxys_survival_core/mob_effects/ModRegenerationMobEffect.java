package lol.roxxane.roxxys_survival_core.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ModRegenerationMobEffect extends ModMobEffect {
	public ModRegenerationMobEffect(MobEffectCategory category, int color) {
		super(category, color);
	}
	public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
		if (livingEntity.getHealth() < livingEntity.getMaxHealth())
			livingEntity.heal(1);
	}
	/// Taken from {@link MobEffect#isDurationEffectTick}
	public boolean isDurationEffectTick(int duration, int amplifier) {
		var k = 50 >> amplifier;
		if (k > 0)
			return duration % k == 0;
		else
			return true;
	}
}
