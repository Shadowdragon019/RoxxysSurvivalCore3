package lol.roxxane.roxxys_survival_core.jei.categories;

import lol.roxxane.roxxys_survival_core.jei.ModJeiRecipeTypes;
import lol.roxxane.roxxys_survival_core.util.Id;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import snownee.kiwi.customization.block.family.BlockFamily;

public class SwitchingCategory extends AbstractRecipeCategory<BlockFamily> {
	public static final ResourceLocation TEXTURE = Id.mod("textures/jei/category/switching.png");
	public SwitchingCategory(IGuiHelper gui_helper) {
		super(
			ModJeiRecipeTypes.SWITCHING,
			Component.translatable("jei.category.roxxys_survival_core.switching"),
			gui_helper.drawableBuilder(TEXTURE, 0, 64, 16, 16).build(),
			142,
			110
		);
	}
	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, BlockFamily recipe, IFocusGroup $) {
		builder.addInputSlot(9999, 9999).addItemStacks(recipe.items().map(Item::getDefaultInstance).toList());
		recipe.items().forEach(item ->
			builder.addOutputSlot().addItemStack(item.getDefaultInstance()));
	}
	public void createRecipeExtras(IRecipeExtrasBuilder builder, BlockFamily recipe, IFocusGroup focuses) {
		var recipeSlots = builder.getRecipeSlots();
		var outputSlots = recipeSlots.getSlots(RecipeIngredientRole.OUTPUT);
		var scrollGridWidget = builder.addScrollGridWidget(outputSlots, 7, 4);
		scrollGridWidget.setPosition(0, 0, getWidth(), getHeight(), HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
	}
	@Override
	public void draw(BlockFamily recipe, IRecipeSlotsView $1, GuiGraphics graphics, double $2, double $3) {
		graphics.blit(TEXTURE, 47, 0, 0, 32, 32, 32);
	}
}
