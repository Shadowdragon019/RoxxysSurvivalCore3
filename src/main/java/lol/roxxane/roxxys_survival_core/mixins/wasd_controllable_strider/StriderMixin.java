package lol.roxxane.roxxys_survival_core.mixins.wasd_controllable_strider;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/// Copyed from {@link net.minecraft.world.entity.animal.horse.AbstractHorse} with slight changes
@Mixin(Strider.class)
abstract class StriderMixin extends Animal {
	protected StriderMixin(EntityType<? extends Animal> $, Level $1) { super($, $1); }
	@Inject(method = "getControllingPassenger", cancellable = true, at = @At("HEAD"))
	private void override_controlling_passenger(CallbackInfoReturnable<LivingEntity> cir) {
		var entity = getFirstPassenger();
		if (entity instanceof LivingEntity living) cir.setReturnValue(living);
		else cir.setReturnValue(null);
	}
	@Inject(method = "tickRidden", cancellable = true, at = @At("HEAD"))
	private void override_tick_ridden(Player player, Vec3 travel_vector, CallbackInfo ci) {
		super.tickRidden(player, travel_vector);
		var vec2 = new Vec2(player.getXRot() * 0.5F, player.getYRot());
		setRot(vec2.y, vec2.x);
		yRotO = yBodyRot = yHeadRot = getYRot();
		ci.cancel();
	}
	@Inject(method = "getRiddenInput", cancellable = true, at = @At("HEAD"))
	private void override_ridden_input(Player player, Vec3 travel_vector, CallbackInfoReturnable<Vec3> cir) {
		float f = player.xxa * 0.5F;
		float f1 = player.zza;
		if (f1 <= 0)
			f1 *= 0.25F;
		cir.setReturnValue(new Vec3(f, 0, f1));
	}
}
