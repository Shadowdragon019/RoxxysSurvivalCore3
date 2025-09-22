package lol.roxxane.roxxys_survival_core.data;

import lol.roxxane.roxxys_survival_core.recipes.NameItemRecipe;
import lol.roxxane.roxxys_survival_core.util.Id;
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
		new NameItemRecipe(Id.mod("name_tag")).save(writer);
		/*new JeiCraftingRecipe(Id.mod("test"),
			NonNullList.of(null, Ingredient.of(Items.GLASS)),
			Ingredient.of(ItemTags.LOGS),
			true,
			Map.of(Id.of("crafting"), List.of(Id.of("oak_planks")))).save(writer);*/
	}
	@SuppressWarnings({"unchecked", "SameParameterValue"})
	protected void shaped(RecipeCategory category, ItemLike output, int count, List<String> patterns,
	List<Object> defines, @Nullable String group, Map<String, CriterionTriggerInstance> unlocked_by) {
		var builder = ShapedRecipeBuilder.shaped(category, output, count).group(group);
		for (var pattern : patterns)
			builder.pattern(pattern);
		var i = 1;
		for (var element : defines) {
			var _char = String.valueOf(i).charAt(0);
			if (element instanceof Ingredient ingredient)
				builder.define(_char, ingredient);
			else if (element instanceof ItemLike item)
				builder.define(_char, item);
			else if (element instanceof TagKey<?> tag)
				builder.define(_char, (TagKey<Item>) tag);
			else throw new IllegalArgumentException(element.getClass().toString());
			i++;
		}
		for (var entry : unlocked_by.entrySet())
			builder.unlockedBy(entry.getKey(), entry.getValue());
		builder.save(writer);
	}
}
