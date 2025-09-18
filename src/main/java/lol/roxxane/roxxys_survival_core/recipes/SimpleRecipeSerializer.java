package lol.roxxane.roxxys_survival_core.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public record SimpleRecipeSerializer<T extends Recipe<?>>(Function<ResourceLocation, T> constructor) implements RecipeSerializer<T>  {
	public T fromJson(ResourceLocation id, JsonObject $1) {
		return constructor.apply(id);
	}
	public @Nullable T fromNetwork(ResourceLocation id, FriendlyByteBuf $1) {
		return constructor.apply(id);
	}
	public void toNetwork(FriendlyByteBuf $, T $1) {}
}
