package lol.roxxane.roxxys_survival_core.mixins.increase_player_interaction_range;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
abstract class PlayerMixin {
	@ModifyReturnValue(method = "createAttributes", at = @At("RETURN"))
	private static AttributeSupplier.Builder changeAttributes(AttributeSupplier.Builder original) {
		return original.add(ForgeMod.BLOCK_REACH.get(), 6).add(ForgeMod.ENTITY_REACH.get(), 6);
	}
}
