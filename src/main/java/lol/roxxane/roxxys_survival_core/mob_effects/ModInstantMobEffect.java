package lol.roxxane.roxxys_survival_core.mob_effects;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class ModInstantMobEffect extends InstantenousMobEffect {
	public ModInstantMobEffect(MobEffectCategory category, int color) {
		super(category, color);
	}
	public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity living, int amplifier, double health) {

	}
	public void applyEffectTick(LivingEntity living, int amplifier) {

	}
}
