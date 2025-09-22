package lol.roxxane.roxxys_survival_core.mixins.modify_firework_star_recipe;

import lol.roxxane.roxxys_survival_core.util.New;
import net.minecraft.world.item.FireworkRocketItem.Shape;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.FireworkStarRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static net.minecraft.world.item.FireworkRocketItem.Shape.LARGE_BALL;
import static net.minecraft.world.item.FireworkRocketItem.Shape.STAR;
import static net.minecraft.world.item.Items.*;

@Mixin(FireworkStarRecipe.class)
abstract class FireworkStarRecipeMixin {
	@Mutable @Shadow @Final private static Ingredient SHAPE_INGREDIENT;
	@Shadow @Final private static Map<Item, Shape> SHAPE_BY_ITEM;
	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void modifyIngredients(CallbackInfo ci) {
		List<ItemLike> newShapeIngredient = Arrays.stream(SHAPE_INGREDIENT.getItems())
			.collect(ArrayList::new, (list, stack) -> list.add(stack.getItem()), ArrayList::addAll);
		for (var item : New.list(SKELETON_SKULL, WITHER_SKELETON_SKULL, DRAGON_HEAD, FIRE_CHARGE, GOLD_NUGGET)) {
			SHAPE_BY_ITEM.remove(item);
			newShapeIngredient.remove(item);
		}
		for (var entry : Map.of(CLAY_BALL, LARGE_BALL, COPPER_INGOT, STAR).entrySet()) {
			var item = entry.getKey();
			var shape = entry.getValue();
			SHAPE_BY_ITEM.put(item, shape);
			newShapeIngredient.add(item);
		}
		SHAPE_INGREDIENT = Ingredient.of(newShapeIngredient.toArray(ItemLike[]::new));
	}
}
