package lol.roxxane.roxxys_survival_core.mixins.slime_changes;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import static java.lang.Math.max;
import static lol.roxxane.roxxys_survival_core.configs.ModServerConfig.consistent_slime_damage;

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
		return consistent_slime_damage;
	}
	@ModifyExpressionValue(method = "isDealsDamage",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/monster/Slime;isTiny()Z"))
	private boolean tiny_slimes_deal_damage(boolean original) {
		return false; // It gets inverted
	}
	@ModifyExpressionValue(method = "dealDamage",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Slime;getSize()I"))
	private int tiny_slimes_have_consistent_reach(int original) {
		return max(original, 2);
	}
}
