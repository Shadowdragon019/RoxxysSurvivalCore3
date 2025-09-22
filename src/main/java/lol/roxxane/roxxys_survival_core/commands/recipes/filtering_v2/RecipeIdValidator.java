package lol.roxxane.roxxys_survival_core.commands.recipes.filtering_v2;

import net.minecraft.resources.ResourceLocation;

public record RecipeIdValidator(String against, IdParser parser, IdValidator validator) {
	public boolean test(ResourceLocation recipeId) {
		return validator.test(parser.parse(recipeId), against);
	}
}
