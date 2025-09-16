package lol.roxxane.roxxys_survival_core.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.commands.recipes.RscRecipesRemoveCommand;
import lol.roxxane.roxxys_survival_core.util.Parsing;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import static lol.roxxane.roxxys_survival_core.configs.ModClientJsonConfig.get_command_gson;
import static net.minecraft.commands.Commands.literal;

public class RscCommand {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext build_context) {
		var rsc = literal("rsc")
			.requires(source -> source.hasPermission(4));
		var dump = literal("dump");
		for (var entry : BuiltInRegistries.REGISTRY.entrySet()) {
			var id = entry.getKey().location();
			var registry = entry.getValue();
			dump.then(literal(id.toString()).executes($ -> {
				var string_builder =
					new StringBuilder("Dumbing registry ").append(id).append('\n');
				for (var element : registry.keySet())
					string_builder.append(element).append('\n');
				Rsc.log(string_builder.toString());
				return 0;
			}));
		}
		var hand = literal("hand")
			.executes(context -> send_to_chat(context, false, false))
			.then(literal("stack")
				.executes(context -> send_to_chat(context, false, false)))
			.then(literal("item")
				.executes(context -> send_to_chat(context, false, true)));
		var inventory = literal("inventory")
			.executes(context -> send_to_chat(context, false, false))
			.then(literal("stack")
				.executes(context -> send_to_chat(context, true, false)))
			.then(literal("item")
				.executes(context -> send_to_chat(context, true, true)));
		var recipes = literal("recipes");
		var remove = literal("remove");
		//var replace = Commands.literal("replace");
		var dev = literal("dev");
		RscRecipesRemoveCommand.register(remove);
		//RscRecipesReplaceCommand.register(replace, build_context);
		RscDevCommand.register(dev);
		recipes.then(remove);
		//recipes.then(replace);
		rsc.then(dump);
		rsc.then(hand);
		rsc.then(inventory);
		rsc.then(recipes);
		rsc.then(dev);
		dispatcher.register(rsc);
	}
	private static int send_to_chat(CommandContext<CommandSourceStack> context, boolean inventory, boolean item) {
		var source = context.getSource();
		var player = source.getPlayer();
		if (player != null) {
			var string = "";
			if (inventory) {
				var array = new JsonArray();
				for (var stack : player.getInventory().items) {
					if (!stack.isEmpty())
						array.add(jsonify_stack(stack, item));
				}
				string = get_command_gson().toJson(array);
			} else string = get_command_gson().toJson(jsonify_stack(player.getMainHandItem(), item));
			player.displayClientMessage(Component.literal(string)
					.withStyle(Style.EMPTY
						.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							Component.literal("Click to copy")))
						.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, string))),
				false);
		}
		return 0;
	}
	private static JsonElement jsonify_stack(ItemStack stack, boolean item) {
		if (item) return jsonify_item(stack.getItem());
		else if (ItemStack.matches(stack, new ItemStack(stack.getItem())))
			return jsonify_item(stack.getItem());
		else {
			var tag = stack.getOrCreateTag().copy();
			var stack_tag = new CompoundTag();
			stack_tag.putString("item", String.valueOf(ForgeRegistries.ITEMS.getKey(stack.getItem())));
			stack_tag.put("tag", tag);
			return Parsing.tag_to_json(stack_tag);
		}
	}
	private static JsonElement jsonify_item(Item item) {
		return new JsonPrimitive(String.valueOf(ForgeRegistries.ITEMS.getKey(item)));
	}
}
