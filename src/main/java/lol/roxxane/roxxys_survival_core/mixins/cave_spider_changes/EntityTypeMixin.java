package lol.roxxane.roxxys_survival_core.mixins.cave_spider_changes;

import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(EntityType.class)
abstract class EntityTypeMixin {
	@ModifyConstant(method = "<clinit>", constant = @Constant(floatValue = 0.7f),
		slice = @Slice(
			from = @At(value = "FIELD",
				target = "Lnet/minecraft/world/entity/EntityType;CAT:Lnet/minecraft/world/entity/EntityType;"),
			to = @At(value = "FIELD",
				target = "Lnet/minecraft/world/entity/EntityType;CAVE_SPIDER:Lnet/minecraft/world/entity/EntityType;")
		)
	)
	private static float increaseScaleWidth(float constant) {
		return 1.25f;
	}
}
