package lol.roxxane.roxxys_survival_core.commands.recipes.filtering_v2;

import net.minecraft.commands.arguments.ResourceOrTagKeyArgument.Result;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

import static java.util.Objects.requireNonNull;

public record RecipeItemsValidator(RecipeItemCollector itemCollector, Result<Item> targetItemOrTag, RegistryAccess access) {
	public boolean test(Recipe<?> recipe) {
		var recipeItems = itemCollector.collect(recipe, access);
		var targetItems = itemsFromItemOrTag(targetItemOrTag);
		return recipeItems.stream().anyMatch(targetItems::contains);
	}
	private static List<Item> itemsFromItemOrTag(Result<Item> itemOrTag) {
		return itemOrTag.unwrap()
			.map(
				itemKey -> List.of(requireNonNull(ForgeRegistries.ITEMS.getValue(itemKey.location()))),
				tagKey -> requireNonNull(ForgeRegistries.ITEMS.tags()).getTag(tagKey).stream().toList());
	}
}