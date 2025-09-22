package lol.roxxane.roxxys_survival_core.commands.recipes.filtering_v2;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RecipeItemCollector {
	INPUTS {
		public List<Item> collect(Recipe<?> recipe, RegistryAccess $) {
			return recipe.getIngredients().stream()
				.flatMap(ingredient -> Arrays.stream(ingredient.getItems()))
				.map(ItemStack::getItem)
				.distinct()
				.toList();
		}
	},
	OUTPUTS {
		public List<Item> collect(Recipe<?> recipe, RegistryAccess access) {
			return List.of(recipe.getResultItem(access).getItem());
		}
	},
	BOTH {
		public List<Item> collect(Recipe<?> recipe, RegistryAccess access) {
			var list = new ArrayList<>(INPUTS.collect(recipe, access));
			list.addAll(OUTPUTS.collect(recipe, access));
			return list;
		}
	};
	public abstract List<Item> collect(Recipe<?> recipe, RegistryAccess access);
}
