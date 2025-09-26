package lol.roxxane.roxxys_survival_core.mixins.minimal_f3;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult.Type;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.List;

import static lol.roxxane.roxxys_survival_core.configs.F3ScreenConfig.survivalMode;
import static net.minecraft.world.level.GameType.ADVENTURE;
import static net.minecraft.world.level.GameType.SURVIVAL;

@Mixin(DebugScreenOverlay.class)
abstract class DebugScreenOverlayMixin {
	@Shadow @Final private Minecraft minecraft;
	@WrapWithCondition(method = "getGameInformation",
		slice = @Slice(
			from = @At(value = "INVOKE", remap = false, target = "Lit/unimi/dsi/fastutil/longs/LongSet;size()I")
		),
		at = {
			@At(value = "INVOKE", ordinal = 0, target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
			@At(value = "INVOKE", ordinal = 2, target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
			@At(value = "INVOKE", ordinal = 3, target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
			@At(value = "INVOKE", ordinal = 4, target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
			@At(value = "INVOKE", ordinal = 5, target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
			@At(value = "INVOKE", ordinal = 7, target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
			@At(value = "INVOKE", ordinal = 10, target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
			@At(value = "INVOKE", ordinal = 11, target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
		}
	)
	private boolean leftRemoveInfo(List<?> instance, Object $) {
		if (!survivalMode)
			return true;
		if (minecraft.gameMode == null)
			return true;
		var gameMode = minecraft.gameMode.getPlayerMode();
		return gameMode != SURVIVAL && gameMode != ADVENTURE;
	}
	@ModifyExpressionValue(method = "getSystemInformation",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/HitResult;getType()Lnet/minecraft/world/phys/HitResult$Type;"))
	private Type rightRemoveBlockInfo(Type original) {
		if (minecraft.gameMode == null)
			return original;
		var gameMode = minecraft.gameMode.getPlayerMode();
		if (survivalMode && (gameMode == SURVIVAL || gameMode == ADVENTURE))
			return Type.ENTITY;
		return original;
	}
	@ModifyExpressionValue(method = "getSystemInformation",
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;crosshairPickEntity:Lnet/minecraft/world/entity/Entity;", opcode = Opcodes.GETFIELD))
	private Entity rightRemoveEntityInfo(Entity original) {
		if (minecraft.gameMode == null)
			return original;
		var gameMode = minecraft.gameMode.getPlayerMode();
		if (survivalMode && (gameMode == SURVIVAL || gameMode == ADVENTURE))
			return null;
		return original;
	}
}
