package lol.roxxane.roxxys_survival_core.mixins.remove_xp;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrb.class)
abstract class ExperienceOrbMixin {
	@Inject(method = "award", cancellable = true, at = @At("HEAD"))
	private static void dontAward(ServerLevel $, Vec3 $1, int $2, CallbackInfo ci) {
		ci.cancel();
	}
}
