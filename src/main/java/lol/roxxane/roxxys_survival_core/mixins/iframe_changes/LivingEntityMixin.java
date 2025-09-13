package lol.roxxane.roxxys_survival_core.mixins.iframe_changes;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static lol.roxxane.roxxys_survival_core.configs.ModServerConfig.override_iframe_functionality;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> $, Level $1) {
		super($, $1);
	}
	@ModifyConstant(method = "hurt",
		constant = @Constant(floatValue = 10))
	private float nothing_bypasses_iframes(float original) {
		if (override_iframe_functionality) return Float.MAX_VALUE;
		else return original;
	}
	@Inject(method = "hurt",
		cancellable = true,
		at = @At(value = "FIELD",
			opcode = Opcodes.PUTFIELD,
			target = "Lnet/minecraft/world/entity/LivingEntity;lastHurt:F"))
	private void block_damage_with_iframes(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (invulnerableTime > 0 && override_iframe_functionality)
			cir.setReturnValue(false);
	}
}
