package lol.roxxane.roxxys_survival_core.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/// {@link lol.roxxane.roxxys_survival_core.mixins.ingredients_can_have_air_items}
///
/// {@link lol.roxxane.roxxys_survival_core.mixins.stone_cutting_merge_recipes}
// TODO: Test the mixin!
public class JeiMillingRecipe extends StonecutterRecipe {
	public final Ingredient items;
	public final Map<ResourceLocation, List<ResourceLocation>> hideRecipes;
	public JeiMillingRecipe(ResourceLocation id, Ingredient items, Map<ResourceLocation, List<ResourceLocation>> hideRecipes) {
		super(id, "", Ingredient.EMPTY, ItemStack.EMPTY);
		this.items = items;
		this.hideRecipes = hideRecipes;
	}
	public RecipeSerializer<JeiMillingRecipe> getSerializer() {
		return ModRecipeSerializers.JEI_MILLING;
	}
	public RecipeType<JeiMillingRecipe> getType() {
		return ModRecipeTypes.JEI_MILLING.get();
	}
	public void save(Consumer<FinishedRecipe> writer) {
		writer.accept(new Finished(getId(), this));
	}
	public static class Serializer implements RecipeSerializer<JeiMillingRecipe> {
		public JeiMillingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			var hideRecipes = new HashMap<ResourceLocation, List<ResourceLocation>>();
			for (var entry : json.get("hide_recipes").getAsJsonObject().entrySet()) {
				if (!(entry.getValue() instanceof JsonArray))
					continue;
				if (!Id.is(entry.getKey()))
					continue;
				var category = Id.of(entry.getKey());
				var recipes = entry.getValue().getAsJsonArray();
				for (var recipe : recipes) {
					if (!recipe.isJsonPrimitive())
						continue;
					var primitive = recipe.getAsJsonPrimitive();
					if (!primitive.isString())
						continue;
					var string = primitive.getAsString();
					if (!Id.is(string))
						continue;
					var removeRecipeId = Id.of(string);
					hideRecipes.computeIfAbsent(category, $ -> new ArrayList<>()).add(removeRecipeId);
				}
			}
			return new JeiMillingRecipe(
				recipeId,
				Ingredient.fromJson(json.get("items")),
				hideRecipes);
		}
		public @Nullable JeiMillingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			return new JeiMillingRecipe(recipeId,
				Ingredient.fromNetwork(buffer),
				buffer.readMap(
					FriendlyByteBuf::readResourceLocation,
					valueBuffer -> valueBuffer.readCollection(ArrayList::new, FriendlyByteBuf::readResourceLocation)));
		}
		public void toNetwork(FriendlyByteBuf buffer, JeiMillingRecipe recipe) {
			recipe.items.toNetwork(buffer);
			buffer.writeMap(recipe.hideRecipes,
				FriendlyByteBuf::writeResourceLocation,
				(valueBuffer, value) -> valueBuffer.writeCollection(value, FriendlyByteBuf::writeResourceLocation));
		}
	}
	public record Finished(ResourceLocation id, JeiMillingRecipe recipe) implements FinishedRecipe {
		public void serializeRecipeData(JsonObject json) {
			json.add("items", recipe.items.toJson());
			var hideRecipes = new JsonObject();
			recipe.hideRecipes.forEach((category, recipes) -> {
				var recipesArray = new JsonArray();
				recipes.forEach(hideRecipe ->
					recipesArray.add(hideRecipe.toString()));
				hideRecipes.add(category.toString(), recipesArray);
			});
			json.add("hide_recipes", hideRecipes);
		}
		public ResourceLocation getId() {
			return id;
		}
		public RecipeSerializer<?> getType() {
			return ModRecipeSerializers.JEI_MILLING;
		}
		public @Nullable JsonObject serializeAdvancement() {
			return null;
		}
		public @Nullable ResourceLocation getAdvancementId() {
			return null;
		}
	}
}