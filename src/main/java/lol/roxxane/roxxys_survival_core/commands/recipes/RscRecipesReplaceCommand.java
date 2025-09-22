package lol.roxxane.roxxys_survival_core.commands.recipes;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument.Result;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import static java.util.Objects.requireNonNull;
import static lol.roxxane.roxxys_survival_core.commands.arguments.NamespaceArgument.namespace;
import static lol.roxxane.roxxys_survival_core.commands.arguments.PathArgument.path;
import static lol.roxxane.roxxys_survival_core.commands.recipes.RscRecipeCommands.*;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.arguments.ResourceLocationArgument.id;
import static net.minecraft.commands.arguments.item.ItemArgument.item;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

public class RscRecipesReplaceCommand {
	// id contains/equals input/result/both item (target) item (replace)
	// id contains/equals input/result/both hand (target) item (replace)
	// id contains/equals input/result/both item (target) hand (replace)
	// id contains/equals input/result/both hand (target) hand (replace)
	// namespace contains/equals input/result/both item (target) item (replace)
	// namespace contains/equals input/result/both hand (target) item (replace)
	// namespace contains/equals input/result/both item (target) hand (replace)
	// namespace contains/equals input/result/both hand (target) hand (replace)
	// path contains/equals input/result/both item (target) item (replace)
	// path contains/equals input/result/both hand (target) item (replace)
	// path contains/equals input/result/both item (target) hand (replace)
	// path contains/equals input/result/both hand (target) hand (replace)
	// any_id input/result/both item (target) item (replace)
	// any_id input/result/both hand (target) item (replace)
	// any_id input/result/both item (target) hand (replace)
	// any_id input/result/both hand (target) hand (replace)
	public static void register(LiteralArgumentBuilder<CommandSourceStack> replace, CommandBuildContext build_context) {
		var any_id = literal("any_id")
			.then(recipe_item_predicate_command()
				.then(literal("target_main_hand")
					.then(literal("result_off_hand")
						.executes(context -> {
							var player = context.getSource().getPlayerOrException();
							return replace(context, get_result(player.getMainHandItem()), get_result(player.getOffhandItem()));
						}))
					.then(literal("result_item")
						.then(argument("result_item", item(build_context)))))
				.then(literal("target_item")
					.then(argument("target_item", item(build_context))
						.then(literal("result_off_hand"))
						.then(literal("result_item")
							.then(argument("result_item", item(build_context)))))));
		var namespace = literal("namespace")
			.then(id_validator_command()
				.then(argument("namespace", namespace())
					.then(recipe_item_predicate_command())));
		var path = literal("path")
			.then(id_validator_command()
				.then(argument("path", path())
					.then(recipe_item_predicate_command())));
		var id = literal("id")
			.then(id_validator_command()
				.then(argument("id", id())
					.then(recipe_item_predicate_command())));
		replace
			.then(any_id)
			.then(namespace)
			.then(path)
			.then(id);
	}
	// input Result -> Result
	// result Result -> Item (error if item)
	// both Result -> Item (error if item)
	private static int replace(CommandContext<CommandSourceStack> context, Result<Item> target, Result<Item> result) {
		var source = context.getSource();
		var level = source.getLevel();
		var recipe_manager = level.getRecipeManager();
		var item_is_empty = Id.is_empty_or_air(target.unwrap().map(ResourceKey::location, TagKey::location));
		var item_exists = target.unwrap().map(
			resource_key -> ITEMS.containsKey(resource_key.location()),
			tag_key -> requireNonNull(ITEMS.tags()).isKnownTagName(tag_key));
		var recipe_item_predicate = recipe_item_predicate_argument(context);
		if (!item_is_empty && !item_exists) {
			Rsc.log(recipe_manager.getRecipes().stream()
				.filter(recipe ->
					recipe instanceof ShapelessRecipe || recipe instanceof ShapedRecipe)
				.filter(recipe -> {
					/*var recipe_id = recipe.getId();
					var recipe_id_pass = id_validator == null || id_parser == null;
					if (!recipe_id_pass)
					recipe_id_pass = id_validator.test(parse_recipe_id(id_parser, recipe_id), parse_target_id(id_parser, context));
					if (!recipe_id_pass) return false;*/
					return recipe_item_predicate.test(recipe, target, level);
				})/*.map(recipe -> recipe_item_predicate.transform_recipe(recipe, target, result))*/.toList());
		}
		return 0;
	}
}
