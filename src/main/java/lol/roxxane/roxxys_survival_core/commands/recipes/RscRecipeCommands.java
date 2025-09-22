package lol.roxxane.roxxys_survival_core.commands.recipes;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import lol.roxxane.roxxys_survival_core.commands.arguments.ResourceResult;
import lol.roxxane.roxxys_survival_core.commands.arguments.TagResult;
import lol.roxxane.roxxys_survival_core.commands.recipes.filtering_v2.RecipeIdValidator;
import lol.roxxane.roxxys_survival_core.commands.recipes.filtering_v2.RecipeItemsValidator;
import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument.Result;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static lol.roxxane.roxxys_survival_core.commands.arguments.CertainEnumsArgument.certain_enums_argument;
import static lol.roxxane.roxxys_survival_core.commands.arguments.NamespaceArgument.get_namespace;
import static lol.roxxane.roxxys_survival_core.commands.arguments.PathArgument.get_path;
import static lol.roxxane.roxxys_survival_core.util.fuck_off_exceptions.FuckOffExceptions.trycrash;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.arguments.ResourceLocationArgument.getId;
import static net.minecraft.commands.arguments.ResourceOrTagKeyArgument.getResourceOrTagKey;
import static net.minecraft.commands.arguments.ResourceOrTagKeyArgument.resourceOrTagKey;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

public class RscRecipeCommands {
	private static final DynamicCommandExceptionType RSC_RECIPE_ERROR = new DynamicCommandExceptionType(item -> Component.literal("RSC RECIPE FAILED"));
	// Argument builders
	public static RequiredArgumentBuilder<CommandSourceStack, Result<Item>> item_command(String name) {
		return argument(name, resourceOrTagKey(Registries.ITEM));
	}
	public static RequiredArgumentBuilder<CommandSourceStack, RecipeItemPredicate> recipe_item_predicate_command() {
		return argument("recipe_item_predicate", certain_enums_argument(RecipeItemPredicate.class));
	}
	public static RequiredArgumentBuilder<CommandSourceStack, IdValidator> id_validator_command() {
		return argument("id_validator", certain_enums_argument(IdValidator.class));
	}
	// Argument getters
	public static Result<Item> item_argument(CommandContext<CommandSourceStack> context) {
		return item_argument("item", context);
	}
	public static Result<Item> item_argument(String name, CommandContext<CommandSourceStack> context) {
		return trycrash(() -> getResourceOrTagKey(context, name, Registries.ITEM, RSC_RECIPE_ERROR));
	}
	public static ItemStack stack_argument(String name, int count, CommandContext<CommandSourceStack> context) {
		return trycrash(() -> ItemArgument.getItem(context, name).createItemStack(count, true));
	}
	public static Result<Item> hand_argument(CommandContext<CommandSourceStack> context, InteractionHand hand) {
		var entity = trycrash(() -> context.getSource().getEntityOrException());
		if (entity instanceof LivingEntity living)
			return get_result(living.getItemInHand(hand));
		throw new IllegalStateException("entity %s is not living".formatted(entity.getClass()));
	}
	@SuppressWarnings("OptionalGetWithoutIsPresent")
	public static Result<Item> get_result(ItemStack stack) {
		if (stack.hasCustomHoverName()) {
			var tag_builder = new StringBuilder();
			stack.getHoverName().visit(content -> {
				tag_builder.append(content);
				return Optional.empty();
			});
			var tag = tag_builder.toString();
			if (Id.is(tag))
				return new TagResult<>(ItemTags.create(Id.of(tag)));
			return new TagResult<>(ItemTags.create(Id.EMPTY));
		}
		return new ResourceResult<>(ITEMS.getResourceKey(stack.getItem()).get());
	}
	public static RecipeItemPredicate recipe_item_predicate_argument(CommandContext<CommandSourceStack> context) {
		return context.getArgument("recipe_item_predicate", RecipeItemPredicate.class);
	}
	public static IdValidator id_validator_argument(CommandContext<CommandSourceStack> context) {
		return context.getArgument("id_validator", IdValidator.class);
	}
	// Parse
	public static String parse_target_id(IdParser parser, CommandContext<CommandSourceStack> context) {
		return switch (parser) {
			case NAMESPACE -> get_namespace(context, "namespace");
			case PATH -> get_path(context, "path");
			case ID -> getId(context, "id").toString();
		};
	}
	public static String parse_recipe_id(IdParser parser, ResourceLocation id) {
		return switch (parser) {
			case NAMESPACE -> id.getNamespace();
			case PATH -> id.getPath();
			case ID -> id.toString();
		};
	}
	public static Stream<Recipe<?>> filterRecipes(List<Recipe<?>> recipes,
    @Nullable RecipeType<?> type,
    @Nullable RecipeIdValidator idValidator,
    @Nullable RecipeItemsValidator itemsValidator) {
		return recipes.stream().filter(recipe -> {
			if (idValidator != null && !idValidator.test(recipe.getId())) return false;
			if (itemsValidator != null && !itemsValidator.test(recipe)) return false;
			if (type != null && recipe.getType() != type) return false;
			return true;
		});
	}
}