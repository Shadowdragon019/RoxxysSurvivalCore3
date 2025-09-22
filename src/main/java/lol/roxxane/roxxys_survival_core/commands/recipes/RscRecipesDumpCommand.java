package lol.roxxane.roxxys_survival_core.commands.recipes;

import com.google.gson.JsonArray;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static lol.roxxane.roxxys_survival_core.commands.arguments.NamespaceArgument.namespace;
import static lol.roxxane.roxxys_survival_core.commands.arguments.PathArgument.path;
import static lol.roxxane.roxxys_survival_core.commands.recipes.IdParser.*;
import static lol.roxxane.roxxys_survival_core.commands.recipes.RscRecipeCommands.*;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.arguments.ResourceLocationArgument.id;

public class RscRecipesDumpCommand {
	private static LiteralArgumentBuilder<CommandSourceStack> builder_parser_argument(String name, IdParser parser, ArgumentType<?> type) {
		return literal(name)
			.then(id_validator_command()
				.then(item_commands(argument(name, type), $ -> parser, RscRecipeCommands::id_validator_argument, RscRecipeCommands::item_argument)
					.executes(context -> dump(context, parser, id_validator_argument(context), null, null))));
	}
	public static void register(LiteralArgumentBuilder<CommandSourceStack> remove) {
		var any_id =
			item_commands(literal("any_id"), $ -> null, $ -> null, RscRecipeCommands::item_argument);
		remove
			.then(any_id)
			.then(builder_parser_argument("namespace", NAMESPACE, namespace()))
			.then(builder_parser_argument("path", PATH, path()))
			.then(builder_parser_argument("id", ID, id()));
	}
	private static int dump(CommandContext<CommandSourceStack> context, @Nullable IdParser id_parser, @Nullable IdValidator id_validator, @Nullable RecipeItemPredicate recipe_item_predicate, @Nullable ResourceOrTagKeyArgument.Result<Item> item_or_tag) {
		var source = context.getSource();
		source.sendSystemMessage(Component.literal("Going through recipes!"));
		var level = context.getSource().getLevel();
		var item_or_tag_exists = RscRecipesRemoveCommand.item_or_tag_exists(item_or_tag);
		var recipeIds = new JsonArray();
		level.getRecipeManager().getRecipes().stream()
			.filter(recipe -> {
				if (!RscRecipesRemoveCommand.validate_recipe_id(recipe, context, id_parser, id_validator)) return false;
				if (recipe_item_predicate == null || item_or_tag == null) return true;
				if (!item_or_tag_exists) return false;
				return recipe_item_predicate.test(recipe, item_or_tag, level);
			})
			.forEach(recipe ->
				recipeIds.add(recipe.getId().toString()));
		var recipeIdsString = recipeIds.toString();
		source.sendSuccess(() -> Component.literal(recipeIdsString)
			.withStyle(Style.EMPTY
				.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to copy")))
				.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, recipeIdsString))), true);
		return 0;
	}
	private static <T extends ArgumentBuilder<CommandSourceStack, T>> T item_commands(T command,
    Function<CommandContext<CommandSourceStack>, @Nullable IdParser> parser,
    Function<CommandContext<CommandSourceStack>, @Nullable IdValidator> validator,
    Function<CommandContext<CommandSourceStack>, ResourceOrTagKeyArgument.Result<Item>> item) {
		return command
			.then(recipe_item_predicate_command()
				.then(literal("item")
					.then(item_command("item")
						.executes(context ->
							dump(context,
								parser.apply(context),
								validator.apply(context),
								recipe_item_predicate_argument(context),
								item.apply(context)))))
				.then(literal("hand")
					.executes(context ->
						dump(context,
							parser.apply(context),
							validator.apply(context),
							recipe_item_predicate_argument(context),
							hand_argument(context, InteractionHand.MAIN_HAND))))
				.then(literal("inventory")
					.executes(context -> {
						var player = requireNonNull(context.getSource().getPlayerOrException());
						player.getInventory().items.stream()
							.filter(stack -> !stack.isEmpty())
							.forEach(stack ->
								dump(context, parser.apply(context), validator.apply(context), recipe_item_predicate_argument(context), get_result(stack)));
						return 0;
					})));
	}
}
