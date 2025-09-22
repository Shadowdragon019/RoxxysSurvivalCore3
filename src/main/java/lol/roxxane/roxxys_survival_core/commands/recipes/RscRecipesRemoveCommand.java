package lol.roxxane.roxxys_survival_core.commands.recipes;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument.Result;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static lol.roxxane.roxxys_survival_core.commands.arguments.NamespaceArgument.namespace;
import static lol.roxxane.roxxys_survival_core.commands.arguments.PathArgument.path;
import static lol.roxxane.roxxys_survival_core.commands.recipes.IdParser.*;
import static lol.roxxane.roxxys_survival_core.commands.recipes.RscRecipeCommands.*;
import static lol.roxxane.roxxys_survival_core.configs.ModServerConfig.rsc_recipes_command_output;
import static lol.roxxane.roxxys_survival_core.util.fuck_off_exceptions.FuckOffExceptions.trycrash;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.arguments.ResourceLocationArgument.id;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

public class RscRecipesRemoveCommand {
	public static final String JSON = """
		{
		\t"type": "forge:false"
		}
		""";
	public static LiteralArgumentBuilder<CommandSourceStack> builder_parser_argument(String name, IdParser parser, ArgumentType<?> type) {
		return literal(name)
			.then(id_validator_command()
				.then(item_commands(argument(name, type), $ -> parser, RscRecipeCommands::id_validator_argument, RscRecipeCommands::item_argument)
					.executes(context -> remove(context, parser, id_validator_argument(context), null, null))));
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
	public static boolean validate_recipe_id(Recipe<?> recipe, CommandContext<CommandSourceStack> context, @Nullable IdParser id_parser, @Nullable IdValidator id_validator) {
		var recipe_id = recipe.getId();
		if (id_validator == null || id_parser == null) return true;
		return id_validator.test(parse_recipe_id(id_parser, recipe_id), parse_target_id(id_parser, context));
	}
	public static boolean item_or_tag_exists(@Nullable Result<Item> item_or_tag) {
		if (item_or_tag == null)
			return false;
		var item_or_tag_is_empty_or_air = Id.is_empty_or_air(item_or_tag.unwrap().map(ResourceKey::location, TagKey::location));
		var item_or_tag_exists = item_or_tag.unwrap().map(
			resource_key -> ITEMS.containsKey(resource_key.location()),
			tag_key -> requireNonNull(ITEMS.tags()).isKnownTagName(tag_key));
		return !item_or_tag_is_empty_or_air && item_or_tag_exists;
	}
	public static List<Result<Item>> remove_nonexistent_items_or_tags(List<Result<Item>> item_or_tags) {
		return item_or_tags.stream().filter(RscRecipesRemoveCommand::item_or_tag_exists).toList();
	}
	public static boolean validate_recipe_items(Recipe<?> recipe, RecipeItemPredicate recipe_item_predicate, List<Result<Item>> item_or_tags, Level level) {
		return item_or_tags.stream().anyMatch(item_or_tag -> recipe_item_predicate.test(recipe, item_or_tag, level));
	}
	public static int remove(CommandContext<CommandSourceStack> context, @Nullable IdParser id_parser, @Nullable IdValidator id_validator, @Nullable RecipeItemPredicate recipe_item_predicate, @Nullable Result<Item> item_or_tag) {
		var source = context.getSource();
		source.sendSystemMessage(Component.literal("Going through recipes!"));
		var level = context.getSource().getLevel();
		var item_or_tag_exists = item_or_tag_exists(item_or_tag);
		var found = new AtomicInteger();
		level.getRecipeManager().getRecipes().stream()
			.filter(recipe -> {
				if (!validate_recipe_id(recipe, context, id_parser, id_validator)) return false;
				if (recipe_item_predicate == null || item_or_tag == null) return true;
				if (!item_or_tag_exists) return false;
				return recipe_item_predicate.test(recipe, item_or_tag, level);
			})
			.map(Recipe::getId).forEach(trycrash(id -> {
				var path = rsc_recipes_command_output
					.resolve(id.getNamespace())
					.resolve("recipes")
					.resolve(id.getPath());
				var parent = path.getParent();
				Files.createDirectories(parent);
				Files.deleteIfExists(Path.of(parent + ".json"));
				var writer = new FileWriter(path + ".json");
				writer.write(JSON);
				writer.close();
				found.getAndIncrement();
			}));
		source.sendSuccess(() -> Component.literal("Found %s recipes!".formatted(found)), true);
		return 0;
	}
	public static <T extends ArgumentBuilder<CommandSourceStack, T>> T item_commands(T command,
	Function<CommandContext<CommandSourceStack>, @Nullable IdParser> parser,
	Function<CommandContext<CommandSourceStack>, @Nullable IdValidator> validator,
	Function<CommandContext<CommandSourceStack>, Result<Item>> item) {
	return command
		.then(recipe_item_predicate_command()
			.then(literal("item")
				.then(item_command("item")
					.executes(context ->
						remove(context,
							parser.apply(context),
							validator.apply(context),
							recipe_item_predicate_argument(context),
							item.apply(context)))))
			.then(literal("hand")
				.executes(context ->
					remove(context,
						parser.apply(context),
						validator.apply(context),
						recipe_item_predicate_argument(context),
						hand_argument(context, InteractionHand.MAIN_HAND))))
			.then(literal("inventory")
				.executes(context -> {
					var player = requireNonNull(context.getSource().getPlayerOrException());
					player.getInventory().items.stream()
						.filter(ItemStack::isEmpty)
						.forEach(stack ->
							remove(context, parser.apply(context), validator.apply(context), recipe_item_predicate_argument(context), get_result(stack)));
					return 0;
				})));
	}
}