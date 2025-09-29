package lol.roxxane.roxxys_survival_core.mixins.increase_player_pickup_range;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
abstract class PlayerMixin extends LivingEntity {
	protected PlayerMixin(EntityType<? extends LivingEntity> $, Level $2) { super($, $2); }
	@ModifyExpressionValue(method = "aiStep",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/phys/AABB;inflate(DDD)Lnet/minecraft/world/phys/AABB;"))
	private AABB inflateHitbox(AABB original) {
		return original.inflate(0.5, 0.5, 0.5);
	}
}
