package lol.roxxane.roxxys_survival_core.commands.recipes;

import net.minecraft.commands.arguments.ResourceOrTagKeyArgument.Result;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

public enum RecipeItemPredicate {
	INPUT {
		public boolean transformed_test(Recipe<?> recipe, List<Item> items, Level level) {
			return !items.isEmpty() && recipe.getIngredients().stream()
				.anyMatch(ingredient -> Arrays.stream(ingredient.getItems())
					.anyMatch(stack -> items.contains(stack.getItem())));
		}
	}, OUTPUT {
		public boolean transformed_test(Recipe<?> recipe, List<Item> items, Level level) {
			return !items.isEmpty() && items.contains(recipe.getResultItem(level.registryAccess()).getItem());
		}
	}, BOTH {
		public boolean transformed_test(Recipe<?> recipe, List<Item> items, Level level) {
			return !items.isEmpty() &&
				(INPUT.transformed_test(recipe, items, level) || OUTPUT.transformed_test(recipe, items, level));
		}
	};
	public boolean test(Recipe<?> recipe, Result<Item> item, Level level) {
		return transformed_test(recipe, item.unwrap().map(
			resource_key -> List.of(requireNonNull(ForgeRegistries.ITEMS.getValue(resource_key.location()))),
			tag_key -> requireNonNull(ForgeRegistries.ITEMS.tags()).getTag(tag_key).stream().toList()), level);
	}
	protected abstract boolean transformed_test(Recipe<?> recipe, List<Item> items, Level level);
}