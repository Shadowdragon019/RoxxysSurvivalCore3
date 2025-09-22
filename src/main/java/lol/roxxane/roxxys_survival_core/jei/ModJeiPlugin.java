package lol.roxxane.roxxys_survival_core.jei;

import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.recipes.JeiCraftingRecipe;
import lol.roxxane.roxxys_survival_core.util.Id;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import java.util.ArrayList;
import java.util.Arrays;

import static lol.roxxane.roxxys_survival_core.configs.ModClientJsonConfig.REMOVE_JEI_CATEGORIES;

@JeiPlugin
public class ModJeiPlugin implements IModPlugin {
	public static final ResourceLocation ID = Id.mod(Rsc.ID);
	public ResourceLocation getPluginUid() {
		return ID;
	}
	@SuppressWarnings("unchecked")
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		var recipeManager = jeiRuntime.getRecipeManager();
		recipeManager.createRecipeCategoryLookup().get()
			.map(IRecipeCategory::getRecipeType)
			.filter(recipeType -> REMOVE_JEI_CATEGORIES.contains(recipeType.getUid()))
			.forEach(recipeManager::hideRecipeCategory);
		recipeManager.createRecipeLookup(RecipeTypes.CRAFTING).get()
			.filter(craftingRecipe -> craftingRecipe instanceof JeiCraftingRecipe)
			.forEach(craftingRecipe -> {
				var jeiCraftingRecipe = (JeiCraftingRecipe) craftingRecipe;
				jeiCraftingRecipe.hideRecipes.forEach((hideCategory, recipesIdsToHide) -> {
					var maybeRecipeType = recipeManager.getRecipeType(hideCategory);
					maybeRecipeType.ifPresent(recipeType -> {
						var recipesToHide = new ArrayList<>();
						recipeManager.createRecipeLookup(recipeType).get().forEach(obj -> {
							if (obj instanceof Recipe<?> recipe)
								if (recipesIdsToHide.contains(recipe.getId()))
									recipesToHide.add(recipe);
						});
						recipeManager.hideRecipes((RecipeType<Object>) recipeType, recipesToHide);
					});
				});
			});
	}
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
		registration.getCraftingCategory().addCategoryExtension(JeiCraftingRecipe.class,
			recipe -> (layout_builder, grid_helper, focus_group) -> {
				if (recipe.isShapeless)
					layout_builder.setShapeless();
				grid_helper.createAndSetInputs(layout_builder, recipe.ingredients.stream().map(ingredient -> Arrays.stream(ingredient.getItems()).toList()).toList(), 3, 3);
				grid_helper.createAndSetOutputs(layout_builder, Arrays.asList(recipe.output.getItems()));
			});
	}
}
