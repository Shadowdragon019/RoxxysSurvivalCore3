package lol.roxxane.roxxys_survival_core.mixins.zombie_changes;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Zombie.class)
abstract class ZombieMixin {
	@ModifyExpressionValue(method = "createAttributes",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/monster/Monster;createMonsterAttributes()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;"))
	private static AttributeSupplier.Builder changeMaxHealth(AttributeSupplier.Builder original) {
		return original.add(Attributes.MAX_HEALTH, 10).add(Attributes.ARMOR, 0);
	}
}
