package lol.roxxane.roxxys_survival_core.jei;

import lol.roxxane.roxxys_survival_core.Rsc;
import mezz.jei.api.recipe.RecipeType;
import snownee.kiwi.customization.block.family.BlockFamily;

public class ModJeiRecipeTypes {
	public static final RecipeType<BlockFamily> SWITCHING = create("switching", BlockFamily.class);
	public static <T> RecipeType<T> create(String path, Class<T> clazz) {
		return RecipeType.create(Rsc.ID, path, clazz);
	}
}
