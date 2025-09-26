package lol.roxxane.roxxys_survival_core.mixins.minimal_f3;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static lol.roxxane.roxxys_survival_core.configs.F3ScreenConfig.survivalMode;
import static net.minecraft.world.level.GameType.ADVENTURE;
import static net.minecraft.world.level.GameType.SURVIVAL;

@Mixin(Gui.class)
abstract class GuiMixin {
	@Shadow @Final protected Minecraft minecraft;

	@ModifyExpressionValue(method = "renderCrosshair",
		at = @At(value = "FIELD",
			target = "Lnet/minecraft/client/Options;renderDebug:Z"))
	private boolean eee(boolean original) {
		if (!survivalMode)
			return original;
		if (minecraft.gameMode == null)
			return original;
		var gameMode = minecraft.gameMode.getPlayerMode();
		if (gameMode == SURVIVAL || gameMode == ADVENTURE)
			return false;
		return original;
	}
}
