package lol.roxxane.roxxys_survival_core.data;

import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
	public ModRecipeProvider(PackOutput output) {
		super(output);
	}
	private Consumer<FinishedRecipe> writer;
	protected void buildRecipes(Consumer<FinishedRecipe> writer) {
		this.writer = writer;
	}
	@SuppressWarnings({"unchecked", "SameParameterValue"})
	protected void shaped(RecipeCategory category, ItemLike output, int count, List<String> patterns,
	List<Object> defines, @Nullable String group, Map<String, CriterionTriggerInstance> unlocked_by) {
		var builder = ShapedRecipeBuilder.shaped(category, output, count).group(group);
		for (var pattern : patterns)
			builder.pattern(pattern);
		var i = 1;
		for (var entry : defines) {
			var _char = String.valueOf(i).charAt(0);
			if (entry instanceof Ingredient ingredient)
				builder.define(_char, ingredient);
			else if (entry instanceof ItemLike item)
				builder.define(_char, item);
			else if (entry instanceof TagKey<?> tag)
				builder.define(_char, (TagKey<Item>) tag);
			else throw new IllegalArgumentException(entry.getClass().toString());
			i++;
		}
		for (var entry : unlocked_by.entrySet())
			builder.unlockedBy(entry.getKey(), entry.getValue());
		builder.save(writer);
	}
}
