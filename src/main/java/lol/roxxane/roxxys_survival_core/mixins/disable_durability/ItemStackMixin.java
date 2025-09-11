package lol.roxxane.roxxys_survival_core.mixins.disable_durability;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import lol.roxxane.roxxys_survival_core.tags.ModItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static lol.roxxane.roxxys_survival_core.configs.ModServerConfig.disable_durability;

@Mixin(ItemStack.class)
abstract class ItemStackMixin {
	@Shadow public abstract Item getItem();
	@ModifyReturnValue(method = "isDamageableItem", at = @At("RETURN"))
	boolean rt$isDamageableItem(boolean original) {
		var is_in_indestructible_tag = getItem().getDefaultInstance().is(ModItemTags.UNBREAKABLE);
		if (is_in_indestructible_tag || disable_durability)
			return false;
		return original;
	}
}
