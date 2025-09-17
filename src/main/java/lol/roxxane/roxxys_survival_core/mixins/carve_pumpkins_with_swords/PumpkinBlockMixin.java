package lol.roxxane.roxxys_survival_core.mixins.carve_pumpkins_with_swords;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.PumpkinBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PumpkinBlock.class)
abstract class PumpkinBlockMixin {
	@ModifyExpressionValue(method = "use",
		at = @At(value = "INVOKE", remap = false, target = "Lnet/minecraft/world/item/ItemStack;canPerformAction(Lnet/minecraftforge/common/ToolAction;)Z"))
	private boolean redirect_shears_check(boolean original, @Local ItemStack stack) {
		return stack.is(ItemTags.SWORDS);
	}
	@ModifyExpressionValue(method = "use",
		at = @At(value = "FIELD", target = "Lnet/minecraft/world/item/Items;SHEARS:Lnet/minecraft/world/item/Item;"))
	private Item redirect_stat_award(Item original, @Local ItemStack stack) {
		return stack.getItem();
	}
}
