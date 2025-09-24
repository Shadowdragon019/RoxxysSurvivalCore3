package lol.roxxane.roxxys_survival_core.mixins.respawn_totem_functionality;

import com.llamalad7.mixinextras.sugar.Local;
import lol.roxxane.roxxys_survival_core.blocks.RespawnTotemBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Player.class)
abstract class PlayerMixin {
	@Inject(method = "findRespawnPositionAndUseSpawnBlock",
		cancellable = true,
		at = @At(value = "INVOKE", ordinal = 0,
			target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"))
	private static void chcekRespawnTotem(ServerLevel serverLevel, BlockPos spawnBlockPos, float playerOrientation, boolean isRespawnForced, boolean respawnAfterWinningTheGame, CallbackInfoReturnable<Optional<Vec3>> cir, @Local BlockState state) {
		if (state.getBlock() instanceof RespawnTotemBlock)
			cir.setReturnValue(RespawnAnchorBlock.findStandUpPosition(EntityType.PLAYER, serverLevel, spawnBlockPos));
	}
}
