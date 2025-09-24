package lol.roxxane.roxxys_survival_core.mixins.water_and_floating_dont_slow_down_dig_speed;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
abstract class PlayerMixin {
	@ModifyExpressionValue(method = "getDigSpeed", at =
		{
			@At(value = "INVOKE",
				target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;hasAquaAffinity(Lnet/minecraft/world/entity/LivingEntity;)Z"),
			@At(value = "INVOKE",
				target = "Lnet/minecraft/world/entity/player/Player;onGround()Z")
		}
	)
	private boolean removeSlowdown(boolean original) {
		return true;
	}
}
