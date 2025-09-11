package lol.roxxane.roxxys_survival_core.mixins.mining_cooldown;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static lol.roxxane.roxxys_survival_core.configs.ModServerConfig.*;

@Mixin(MultiPlayerGameMode.class)
abstract class MultiPlayerGameModeMixin {
	@Shadow
	private GameType localPlayerMode;
	@ModifyConstant(method = "continueDestroyBlock", constant = @Constant(intValue = 5))
	private int continueDestroyBlock(int value) {
		if (localPlayerMode.isSurvival()) return survival_mining_cooldown;
		else return creative_mining_cooldown;
	}
}
