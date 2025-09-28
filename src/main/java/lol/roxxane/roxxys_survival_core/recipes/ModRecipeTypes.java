package lol.roxxane.roxxys_survival_core.recipes;

import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {
	public static final DeferredRegister<RecipeType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Rsc.ID);
	public static final RegistryObject<RecipeType<JeiMillingRecipe>> JEI_MILLING = register("jei_milling");
	private static <R extends Recipe<?>> RegistryObject<RecipeType<R>> register(String path) {
		var type = new RecipeType<R>() {
			public String toString() {
				return Id.mod(path).toString();
			}
		};
		return REGISTRY.register(path, () -> type);
	}
}
