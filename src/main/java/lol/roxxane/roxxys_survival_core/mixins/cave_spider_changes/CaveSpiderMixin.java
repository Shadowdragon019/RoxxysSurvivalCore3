package lol.roxxane.roxxys_survival_core.mixins.cave_spider_changes;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CaveSpider.class)
abstract class CaveSpiderMixin extends Spider {
	public CaveSpiderMixin(EntityType<? extends Spider> $, Level $1) {super($, $1);}
	@Redirect(method = "doHurtTarget",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"))
	private boolean disablePoison(LivingEntity $, MobEffectInstance $1, Entity $2) {
		return true;
	}
	public void aiStep() {
		super.aiStep();
		if (!isAlive()) return;
		if (!isSunBurnTick()) return;
		setSecondsOnFire(8);
	}
	@ModifyConstant(method = "createCaveSpider", constant = @Constant(doubleValue = 12))
	private static double reduceHealth(double constant) {
		return 5;
	}
}
