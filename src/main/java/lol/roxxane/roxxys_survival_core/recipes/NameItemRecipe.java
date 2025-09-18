package lol.roxxane.roxxys_survival_core.recipes;

import com.google.gson.JsonObject;
import lol.roxxane.roxxys_survival_core.util.ComponentUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class NameItemRecipe extends CustomRecipe {
	public NameItemRecipe(ResourceLocation id) {
		super(id, CraftingBookCategory.MISC);
	}
	public boolean matches(CraftingContainer container, Level $) {
		var foundNameTag = false;
		var foundItemToRename = false;
		for (var stack : container.getItems()) {
			if (stack.isEmpty()) continue;
			if (stack.is(Items.NAME_TAG)) {
				if (foundNameTag) return false;
				if (!stack.hasCustomHoverName()) return false;
				foundNameTag = true;
			} else {
				if (foundItemToRename) return false;
				foundItemToRename = true;
			}
		}
		return foundNameTag && foundItemToRename;
	}
	public ItemStack assemble(CraftingContainer container, RegistryAccess $) {
		ItemStack nameTag = null;
		ItemStack targetStack = null;
		for (var stack : container.getItems()) {
			if (stack.isEmpty()) continue;
			if (stack.is(Items.NAME_TAG)) {
				if (nameTag == null) nameTag = stack;
				else return ItemStack.EMPTY;
			} else {
				if (targetStack == null) targetStack = stack;
				else return ItemStack.EMPTY;
			}
		}
		if (nameTag == null || targetStack == null) return ItemStack.EMPTY;
		return targetStack.copy().setHoverName(Component.literal(ComponentUtil.componentToString(nameTag.getHoverName())));
	}
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.NAME_ITEM;
	}
	public void save(Consumer<FinishedRecipe> writer) {
		writer.accept(new Finished(getId()));
	}
	public record Finished(ResourceLocation id) implements FinishedRecipe {
		public void serializeRecipeData(JsonObject $) {}
		public @NotNull ResourceLocation getId() {
			return id;
		}
		public RecipeSerializer<?> getType() {
			return ModRecipeSerializers.NAME_ITEM;
		}
		public @Nullable JsonObject serializeAdvancement() {
			return null;
		}
		public @Nullable ResourceLocation getAdvancementId() {
			return null;
		}
	}
}
