package lol.roxxane.roxxys_survival_core.mixins.no_attack_strength_cooldown;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
abstract class PlayerMixin {
	@Inject(method = "resetAttackStrengthTicker", cancellable = true,
		at = @At("HEAD"))
	private void dontResetAttackStrengthTicker(CallbackInfo ci) {
		ci.cancel();
	}
}
