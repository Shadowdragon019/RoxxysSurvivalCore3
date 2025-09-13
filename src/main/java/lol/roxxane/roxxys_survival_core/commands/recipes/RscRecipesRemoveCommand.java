package lol.roxxane.roxxys_survival_core.commands.recipes;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument.ResourceResult;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument.Result;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument.TagResult;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

import static lol.roxxane.roxxys_survival_core.commands.arguments.LowercaseEnumArgument.lowercase_enum_argument;
import static lol.roxxane.roxxys_survival_core.commands.arguments.NamespaceArgument.get_namespace;
import static lol.roxxane.roxxys_survival_core.commands.arguments.NamespaceArgument.namespace;
import static lol.roxxane.roxxys_survival_core.commands.arguments.PathArgument.get_path;
import static lol.roxxane.roxxys_survival_core.commands.arguments.PathArgument.path;
import static lol.roxxane.roxxys_survival_core.commands.recipes.IdParser.*;
import static lol.roxxane.roxxys_survival_core.util.fuck_off_exceptions.FuckOffExceptions.trycrash;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.arguments.ResourceLocationArgument.getId;
import static net.minecraft.commands.arguments.ResourceLocationArgument.id;
import static net.minecraft.commands.arguments.ResourceOrTagKeyArgument.getResourceOrTagKey;
import static net.minecraft.commands.arguments.ResourceOrTagKeyArgument.resourceOrTagKey;
import static net.minecraftforge.fml.loading.FMLPaths.GAMEDIR;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

// TODO: Into make this go into any ol' path (open loader~)
// TODO: Inventory variant
public class RscRecipesRemoveCommand {
	public static final Path OUTPUT = Path.of(GAMEDIR.get().toString() + "/rsc_output/recipes");
	public static final String JSON = """
		{
		  "type": "forge:false"
		}
		""";
	public static void register_remove(LiteralArgumentBuilder<CommandSourceStack> remove) {
		var any =
			item_commands(literal("any"), $ -> null, $ -> null, RscRecipesRemoveCommand::item_argument);
		var namespace =
			literal("namespace")
				.then(id_validator_command()
					.then(item_commands(argument("namespace", namespace()), $ -> NAMESPACE, RscRecipesRemoveCommand::id_validator_argument, RscRecipesRemoveCommand::item_argument)
						.executes(context -> remove(context, NAMESPACE, id_validator_argument(context), null, null))));
		var path =
			literal("path")
				.then(id_validator_command()
					.then(item_commands(argument("path", path()), $ -> PATH, RscRecipesRemoveCommand::id_validator_argument, RscRecipesRemoveCommand::item_argument)
						.executes(context -> remove(context, PATH, id_validator_argument(context), null, null))));
		var id =
			literal("id")
				.then(id_validator_command()
					.then(item_commands(argument("id", id()), $ -> ID, RscRecipesRemoveCommand::id_validator_argument, RscRecipesRemoveCommand::item_argument)
						.executes(context -> remove(context, ID, id_validator_argument(context), null, null))));
		remove
			.then(any)
			.then(namespace)
			.then(path)
			.then(id);
	}
	private static final DynamicCommandExceptionType REMOVE_ERROR = new DynamicCommandExceptionType(item -> Component.literal("REMOVE FAILED"));
	private static int remove(CommandContext<CommandSourceStack> context,
	@Nullable IdParser id_parser,
	@Nullable IdValidator id_validator,
	@Nullable RecipeItemPredicate recipe_item_predicate,
	@Nullable Result<Item> item) {
		throw new NotImplementedException();
		/*message(context, "AAAAAAAAAAAAAAAAAAAAAA");
		context.getSource().sendSystemMessage(Component.literal("Going through recipes!"));
		var level = context.getSource().getLevel();
		var item_is_empty = item != null && Id.is_empty_or_air(item.unwrap().map(ResourceKey::location, TagKey::location));
		var item_exists = item != null && item.unwrap().map(
			resource_key -> ITEMS.containsKey(resource_key.location()),
			tag_key -> requireNonNull(ITEMS.tags()).isKnownTagName(tag_key));
		var found = new AtomicInteger();
		for (var id : level.getRecipeManager().getRecipes().stream()
			.filter(recipe -> {
				var recipe_id = recipe.getId();
				var recipe_id_pass = id_validator == null || id_parser == null;
				if (!recipe_id_pass)
					recipe_id_pass = id_validator.test(parse_recipe_id(id_parser, recipe_id), parse_target_id(id_parser, context));
				if (!recipe_id_pass) return false;
				if (recipe_item_predicate == null || item == null) return true;
				if (item_is_empty || ! item_exists) return false;
				return recipe_item_predicate.test(recipe, item, level);
			})
			.map(Recipe::getId)
			.toList()
		) {
			trycrash(() -> {
				var path = OUTPUT
					.resolve(id.getNamespace())
					.resolve("recipes")
					.resolve(id.getPath());
				var parent = path.getParent();
				Files.createDirectories(parent);
				var writer = new FileWriter(path + ".json");
				writer.write(JSON);
				writer.close();
				found.getAndIncrement();
			});
		}
		context.getSource().sendSuccess(() -> Component.literal("Found %s recipes!".formatted(found)), true);
		return 0;*/
	}
	// Argument builders
	private static RequiredArgumentBuilder<CommandSourceStack, Result<Item>> item_command() {
		return argument("item", resourceOrTagKey(Registries.ITEM));
	}
	private static LiteralArgumentBuilder<CommandSourceStack> hand_command() {
		return literal("hand");
	}
	private static <E extends Enum<E>> RequiredArgumentBuilder<CommandSourceStack, E> enum_command(String name, Class<E> clazz) {
		return argument(name, lowercase_enum_argument(clazz));
	}
	private static RequiredArgumentBuilder<CommandSourceStack, RecipeItemPredicate> recipe_item_predicate_command() {
		return enum_command("recipe_item_predicate", RecipeItemPredicate.class);
	}
	private static RequiredArgumentBuilder<CommandSourceStack, IdValidator> id_validator_command() {
		return enum_command("id_validator", IdValidator.class);
	}
	//rsc recipes remove any both hand
	private static <T extends ArgumentBuilder<CommandSourceStack, T>> T item_commands(T command,
	Function<CommandContext<CommandSourceStack>, @Nullable IdParser> parser,
	Function<CommandContext<CommandSourceStack>, @Nullable IdValidator> validator,
	Function<CommandContext<CommandSourceStack>, Result<Item>> item) {
	return command
		.then(recipe_item_predicate_command()
			.then(literal("item")
				.then(item_command()
					.executes(context ->
						remove(context,
							parser.apply(context),
							validator.apply(context),
							recipe_item_predicate_argument(context),
							item.apply(context)))))
			.then(hand_command()
				.executes(context -> {
					message(context, "HAAAAND");
					remove(context,
						parser.apply(context),
						validator.apply(context),
						recipe_item_predicate_argument(context),
						hand_argument(context));
					return 0;
				})));
	}
	// Argument getters
	private static Result<Item> item_argument(CommandContext<CommandSourceStack> context) {
		return trycrash(() -> getResourceOrTagKey(context, "item", Registries.ITEM, REMOVE_ERROR));
	}
	@SuppressWarnings("OptionalGetWithoutIsPresent")
	private static Result<Item> hand_argument(CommandContext<CommandSourceStack> context) {
		message(context, "CCCCCCCCCCCCCC");
		var entity = trycrash(() -> context.getSource().getEntityOrException());
		if (entity instanceof LivingEntity living) {
			var stack = living.getMainHandItem();
			if (stack.hasCustomHoverName()) {
				var tag_builder = new StringBuilder();
				stack.getHoverName().visit(content -> {
					tag_builder.append(content);
					return Optional.empty();
				});
				var tag = tag_builder.toString();
				if (Id.is(tag))
					return new TagResult<>(ItemTags.create(Id.of(tag)));
				message(context, "DDDDDDDDDDDDDDD");
				return new TagResult<>(ItemTags.create(Id.EMPTY));
			}
			message(context, "EEEEEEEEEEE");
			return new ResourceResult<>(ITEMS.getResourceKey(stack.getItem()).get());
		}
		message(context, "FFFFFFFFFFF");
		throw new IllegalStateException("entity %s is not living".formatted(entity.getClass()));
	}
	private static RecipeItemPredicate recipe_item_predicate_argument(CommandContext<CommandSourceStack> context) {
		message(context, "BBBBBBBBBBBBBBB");
		return context.getArgument("recipe_item_predicate", RecipeItemPredicate.class);
	}
	private static IdValidator id_validator_argument(CommandContext<CommandSourceStack> context) {
		return context.getArgument("id_validator", IdValidator.class);
	}
	private static String parse_target_id(IdParser parser, CommandContext<CommandSourceStack> context) {
		return switch (parser) {
			case NAMESPACE -> get_namespace(context, "namespace");
			case PATH -> get_path(context, "path");
			case ID -> getId(context, "id").toString();
		};
	}
	private static String parse_recipe_id(IdParser parser, ResourceLocation id) {
		return switch (parser) {
			case NAMESPACE -> id.getNamespace();
			case PATH -> id.getPath();
			case ID -> id.toString();
		};
	}
	private static void message(CommandContext<CommandSourceStack> context, String message) {
		context.getSource().sendSystemMessage(Component.literal(message));
	}
}
