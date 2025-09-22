package lol.roxxane.roxxys_survival_core.mixins.ingredients_can_have_air_items;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapedRecipe.class)
abstract class ShapeRecipeMixin {
	@Inject(method = "itemFromJson",
		cancellable = true,
		at = @At(value = "FIELD",
			target = "Lnet/minecraft/world/item/Items;AIR:Lnet/minecraft/world/item/Item;"))
	private static void returnEarly(JsonObject $, CallbackInfoReturnable<Item> cir, @Local Item item) {
		cir.setReturnValue(item);
	}
}
