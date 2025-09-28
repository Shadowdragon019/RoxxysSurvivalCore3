package lol.roxxane.roxxys_survival_core.mixins.stone_cutting_merge_recipes;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lol.roxxane.roxxys_survival_core.recipes.JeiMillingRecipe;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.library.plugins.vanilla.stonecutting.StoneCuttingRecipeCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(StoneCuttingRecipeCategory.class)
abstract class StoneCuttingRecipeCategoryMixin {
	@WrapOperation(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lnet/minecraft/world/item/crafting/StonecutterRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
		remap = false,
		slice = @Slice(
			from = @At(value = "INVOKE", target = "Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;addOutputSlot(II)Lmezz/jei/api/gui/builder/IRecipeSlotBuilder;")
		),
		at = @At(value = "INVOKE",
			target = "Lmezz/jei/api/gui/builder/IRecipeSlotBuilder;addItemStack(Lnet/minecraft/world/item/ItemStack;)Lmezz/jei/api/gui/builder/IIngredientAcceptor;"))
	private IIngredientAcceptor jeiRecipesOutput(IRecipeSlotBuilder instance, ItemStack stack, Operation<IIngredientAcceptor> original, @Local(argsOnly = true) StonecutterRecipe recipe) {
		if (recipe instanceof JeiMillingRecipe millingRecipe)
			instance.addIngredients(millingRecipe.items);
		else original.call(instance, stack);
		return instance;
	}
	@ModifyExpressionValue(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lnet/minecraft/world/item/crafting/StonecutterRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
		remap = false,
		at = @At(value = "INVOKE",
			remap = true,
			target = "Lnet/minecraft/core/NonNullList;get(I)Ljava/lang/Object;"))
	private Object jeiRecipesOutput(Object original, @Local(argsOnly = true) StonecutterRecipe recipe) {
		if (recipe instanceof JeiMillingRecipe millingRecipe)
			return millingRecipe.items;
		return original;
	}
}
