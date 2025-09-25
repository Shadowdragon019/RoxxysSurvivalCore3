package lol.roxxane.roxxys_survival_core.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import lol.roxxane.roxxys_survival_core.configs.ModClientJsonConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import static lol.roxxane.roxxys_survival_core.configs.ModClientJsonConfig.TABS_CLEAR;
import static net.minecraft.commands.Commands.literal;

public class RscDevCommand {
	public static void register(LiteralArgumentBuilder<CommandSourceStack> dev) {
		dev.then(literal("tab_modifications")
			.then(literal("clear")
				.executes(context -> {
					for (var tab : TABS_CLEAR)
						context.getSource().sendSystemMessage(Component.literal(tab.toString()));
					return 0;
				}))
			.then(literal("remove_pre")
				.executes(context -> {
					for (var stacks : ModClientJsonConfig.TABS_REMOVE_PRE.values())
						for (var stack : stacks)
							spawn_stack(context, stack);
					return 0;
				}))
			.then(literal("add_end")
				.executes(context -> {
					for (var stacks : ModClientJsonConfig.TABS_ADD_END.values())
						for (var stack : stacks)
							spawn_stack(context, stack);
					return 0;
				}))
			.then(literal("add_start")
				.executes(context -> {
					for (var stacks : ModClientJsonConfig.TABS_ADD_START.values())
						for (var stack : stacks)
							spawn_stack(context, stack);
					return 0;
				}))
			.then(literal("add_after")
				.executes(context -> {
					for (var stacks_map : ModClientJsonConfig.TABS_ADD_AFTER.values())
						for (var stacks : stacks_map.values())
							for (var stack : stacks)
								spawn_stack(context, stack);
					return 0;
				}))
			.then(literal("add_before")
				.executes(context -> {
					for (var stacks_map : ModClientJsonConfig.TABS_ADD_BEFORE.values())
						for (var stacks : stacks_map.values())
							for (var stack : stacks)
								spawn_stack(context, stack);
					return 0;
				}))
			.then(literal("remove_post")
				.executes(context -> {
					for (var stacks : ModClientJsonConfig.TABS_REMOVE_POST.values())
						for (var stack : stacks)
							spawn_stack(context, stack);
					return 0;
				}))
		)/*.then(literal("replace_smelting_recipes").executes(context -> {
			var source = context.getSource();
			var level = source.getLevel();
			level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING).forEach(trycrash(recipe -> {
				var newRecipe = smelting(recipe.getIngredients().get(0), recipe.category(), recipe.getResultItem(level.registryAccess()).getItem(), 0, 60).group(recipe.getGroup());

				var path = rsc_recipes_command_output
					.resolve(recipe.getId().getNamespace())
					.resolve("recipes")
					.resolve(recipe.getId().getPath());
				var parent = path.getParent();
				Files.createDirectories(parent);
				Files.deleteIfExists(Path.of(parent + ".json"));
				var writer = new FileWriter(path + ".json");
				writer.write();
				writer.close();
			}));
			return 0;
		}))*/;
	}
	private static void spawn_stack(CommandContext<CommandSourceStack> context, ItemStack stack) {
		var source = context.getSource();
		var pos = source.getPosition();
		var level = source.getLevel();
		level.addFreshEntity(new ItemEntity(level, pos.x, pos.y, pos.z, stack));
	}
}
