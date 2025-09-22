package lol.roxxane.roxxys_survival_core.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/// {@link lol.roxxane.roxxys_survival_core.mixins.ingredients_can_have_air_items.ShapedRecipeMixin}
public class JeiCraftingRecipe extends CustomRecipe {
	public final NonNullList<Ingredient> ingredients;
	public final Ingredient output;
	public final boolean isShapeless;
	public final Map<ResourceLocation, List<ResourceLocation>> hideRecipes;
	public JeiCraftingRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, Ingredient output, boolean isShapeless, Map<ResourceLocation, List<ResourceLocation>> hideRecipes) {
		super(id, CraftingBookCategory.MISC);
		this.ingredients = ingredients;
		this.output = output;
		this.isShapeless = isShapeless;
		this.hideRecipes = hideRecipes;
	}
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}
	public boolean matches(CraftingContainer $, Level $1) {
		return false;
	}
	public ItemStack assemble(CraftingContainer $, RegistryAccess $1) {
		return ItemStack.EMPTY;
	}
	public boolean canCraftInDimensions(int $, int $1) {
		return false;
	}
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.JEI_CRAFTING;
	}
	public void save(Consumer<FinishedRecipe> writer) {
		writer.accept(new Finished(getId(), this));
	}
	public static class Serializer implements RecipeSerializer<JeiCraftingRecipe> {
		public JeiCraftingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
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
			return new JeiCraftingRecipe(
				recipeId,
				itemsFromJson(json.get("ingredients").getAsJsonArray()),
				Ingredient.fromJson(json.get("output")),
				json.get("shapeless").getAsBoolean(),
				hideRecipes);
			/*return new JeiCraftingRecipe(
				recipeId,
				itemsFromJson(json.get("ingredients").getAsJsonArray()),
				ingredientFromJsonThatAllowsEmpty(json.get("output")),
				json.get("shapeless").getAsBoolean(),
				hideRecipes);*/
		}
		public static NonNullList<Ingredient> itemsFromJson(JsonArray ingredientArray) {
			var list = NonNullList.<Ingredient>create();
			for (var ingredient : ingredientArray)
				list.add(Ingredient.fromJson(ingredient));
			return list;
		}
		/*public static Item itemFromJson(JsonObject jsonObject) {
			var itemString = GsonHelper.getAsString(jsonObject, "item");
			return BuiltInRegistries.ITEM.getOptional(ResourceLocation.tryParse(itemString))
				.orElseThrow(() -> new JsonSyntaxException("Unknown item '" + itemString + "'"));
		}
		public static Ingredient.Value valueFromJson(JsonObject pJson) {
			if (pJson.has("item") && pJson.has("tag")) {
				throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
			} else if (pJson.has("item")) {
				var item = itemFromJson(pJson);
				return new Ingredient.ItemValue(new ItemStack(item));
			} else if (pJson.has("tag")) {
				var id = Id.of(GsonHelper.getAsString(pJson, "tag"));
				var tagkey = TagKey.create(Registries.ITEM, id);
				return new Ingredient.TagValue(tagkey);
			} else throw new JsonParseException("An ingredient entry needs either a tag or an item");
		}
		public static Ingredient ingredientFromJsonThatAllowsEmpty(JsonElement json) {
			if (!json.isJsonNull()) {
				@Nullable var ret = CraftingHelper.getIngredient(json, true);
				if (ret != null) return ret;
				if (json.isJsonObject()) {
					return fromValues(Stream.of(valueFromJson(json.getAsJsonObject())));
				} else if (json.isJsonArray()) {
					var array = json.getAsJsonArray();
					return fromValues(StreamSupport.stream(array.spliterator(), false)
						.map(jsonElement -> valueFromJson(GsonHelper.convertToJsonObject(jsonElement, "item"))));
				} else throw new JsonSyntaxException("Expected item to be object or array of objects");
			} else throw new JsonSyntaxException("Item cannot be null");
		}
		public static NonNullList<Ingredient> itemsFromJson(JsonArray ingredientArray) {
			var list = NonNullList.<Ingredient>create();
			for (var ingredient : ingredientArray)
				list.add(ingredientFromJsonThatAllowsEmpty(ingredient));
			return list;
		}*/
		public @Nullable JeiCraftingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			return new JeiCraftingRecipe(recipeId,
				buffer.readCollection(NonNullList::createWithCapacity, Ingredient::fromNetwork),
				Ingredient.fromNetwork(buffer),
				buffer.readBoolean(),
				// HOLY FUCK I HOPE THIS WORKS
				buffer.readMap(
					FriendlyByteBuf::readResourceLocation,
					valueBuffer -> valueBuffer.readCollection(ArrayList::new, FriendlyByteBuf::readResourceLocation)));
		}
		public void toNetwork(FriendlyByteBuf buffer, JeiCraftingRecipe recipe) {
			buffer.writeCollection(recipe.ingredients,
				(ingredientBuffer, ingredient) -> ingredient.toNetwork(ingredientBuffer));
			recipe.output.toNetwork(buffer);
			buffer.writeBoolean(recipe.isShapeless);
			// HOLY FUCK I HOPE THIS WORKS
			buffer.writeMap(recipe.hideRecipes,
				FriendlyByteBuf::writeResourceLocation,
				(valueBuffer, value) -> valueBuffer.writeCollection(value, FriendlyByteBuf::writeResourceLocation));
		}
	}
	public record Finished(ResourceLocation id, JeiCraftingRecipe recipe) implements FinishedRecipe {
		public void serializeRecipeData(JsonObject json) {
			var ingredients = new JsonArray();
			for (var ingredient : recipe.ingredients)
				ingredients.add(ingredient.toJson());
			json.add("ingredients", ingredients);
			json.add("output", recipe.output.toJson());
			json.addProperty("shapeless", recipe.isShapeless); //HIDE RECIPES
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
			return ModRecipeSerializers.JEI_CRAFTING;
		}
		public @Nullable JsonObject serializeAdvancement() {
			return null;
		}
		public @Nullable ResourceLocation getAdvancementId() {
			return null;
		}
	}
}