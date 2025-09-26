package lol.roxxane.roxxys_survival_core.mob_effects;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import static lol.roxxane.roxxys_survival_core.mob_effects.ModMobEffects.WITCH_INSTANT_DAMAGE;
import static lol.roxxane.roxxys_survival_core.mob_effects.ModMobEffects.WITCH_INSTANT_HEALTH;

public class ModInstantMobEffect extends InstantenousMobEffect {
	public ModInstantMobEffect(MobEffectCategory category, int color) {
		super(category, color);
	}
	public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity living, int amplifier, double health) {
		if (!living.canBeAffected(new MobEffectInstance(this))) return;
		if (this == WITCH_INSTANT_DAMAGE.get())
			if (source == null)
				living.hurt(living.damageSources().magic(), (amplifier + 1) * 2);
			else
				living.hurt(living.damageSources().indirectMagic(source, indirectSource), (amplifier + 1) * 2);
		else if (this == WITCH_INSTANT_HEALTH.get())
			living.heal((amplifier + 1) * 2);
	}
	public void applyEffectTick(LivingEntity living, int amplifier) {
		if (this == WITCH_INSTANT_DAMAGE.get())
			living.hurt(living.damageSources().magic(), (amplifier + 1) * 2);
		else if (this == WITCH_INSTANT_HEALTH.get())
			living.heal((amplifier + 1) * 2);
	}
}
