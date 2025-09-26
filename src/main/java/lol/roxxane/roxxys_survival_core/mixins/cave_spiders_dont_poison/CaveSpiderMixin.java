package lol.roxxane.roxxys_survival_core.mixins.cave_spiders_dont_poison;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CaveSpider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CaveSpider.class)
abstract class CaveSpiderMixin {
	@Redirect(method = "doHurtTarget",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"))
	private boolean disablePoison(LivingEntity $, MobEffectInstance $1, Entity $2) {
		return true;
	}
}
