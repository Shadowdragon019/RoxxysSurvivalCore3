package lol.roxxane.roxxys_survival_core.mixins.slime_changes;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import static java.lang.Math.max;
import static lol.roxxane.roxxys_survival_core.configs.ModServerConfig.consistent_slime_damage;
import static lol.roxxane.roxxys_survival_core.configs.ModServerConfig.tiny_slimes_deal_damage;

@Mixin(Slime.class)
abstract class SlimeMixin {
	@ModifyArg(method = "setSize",
		slice = @Slice(
			from = @At(value = "FIELD",
				target = "Lnet/minecraft/world/entity/ai/attributes/Attributes;ATTACK_DAMAGE:Lnet/minecraft/world/entity/ai/attributes/Attribute;")
		),
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;setBaseValue(D)V"))
	private double consistent_slime_damage(double original) {
		if (consistent_slime_damage > -1)
			return consistent_slime_damage;
		return original;
	}
	@ModifyExpressionValue(method = "isDealsDamage",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/monster/Slime;isTiny()Z"))
	private boolean tiny_slimes_deal_damage(boolean original) {
		if (tiny_slimes_deal_damage)
			return false; // It gets inverted
		return original;
	}
	@ModifyExpressionValue(method = "dealDamage",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Slime;getSize()I"))
	private int tiny_slimes_have_consistent_reach(int original) {
		if (tiny_slimes_deal_damage)
			return max(original, 2);
		return original;
	}
}
