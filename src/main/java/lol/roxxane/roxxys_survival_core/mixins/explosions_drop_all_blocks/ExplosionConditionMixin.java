package lol.roxxane.roxxys_survival_core.mixins.explosions_drop_all_blocks;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExplosionCondition.class)
abstract class ExplosionConditionMixin {
	@Inject(method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z",
		cancellable = true, at = @At("HEAD"))
	private void alwaysDropBlock(LootContext pContext, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
	}
}
