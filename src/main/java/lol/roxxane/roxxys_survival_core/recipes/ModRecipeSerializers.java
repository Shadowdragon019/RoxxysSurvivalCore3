package lol.roxxane.roxxys_survival_core.recipes;

import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class ModRecipeSerializers {
	public static SimpleRecipeSerializer<NameItemRecipe> NAME_ITEM = register_simple("name_item", NameItemRecipe::new);
	@SuppressWarnings("unchecked")
	private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String key, S serializer) {
		ForgeRegistries.RECIPE_SERIALIZERS.register(Id.mod(key), serializer);
		return (S) requireNonNull(ForgeRegistries.RECIPE_SERIALIZERS.getValue(Id.mod(key)));
	}
	private static <T extends Recipe<?>> SimpleRecipeSerializer<T> register_simple(String key, Function<ResourceLocation, T> constructor) {
		return register(key, new SimpleRecipeSerializer<>(constructor));
	}
	public static void register() {}
}
