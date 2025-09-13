package lol.roxxane.roxxys_survival_core.commands;

import com.google.gson.*;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.commands.recipes.RscRecipesRemoveCommand;
import lol.roxxane.roxxys_survival_core.util.Parsing;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class RscCommand {
	public static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext build_context) {
		var rsc = Commands.literal("rsc")
			.requires(source -> source.hasPermission(4));
		var dump = Commands.literal("dump");
		for (var entry : BuiltInRegistries.REGISTRY.entrySet()) {
			var id = entry.getKey().location();
			var registry = entry.getValue();
			dump.then(Commands.literal(id.toString()).executes($ -> {
				var string_builder =
					new StringBuilder("Dumbing registry ").append(id).append('\n');
				for (var element : registry.keySet())
					string_builder.append(element).append('\n');
				Rsc.log(string_builder.toString());
				return 0;
			}));
		}
		var hand = Commands.literal("hand");
		var inventory = Commands.literal("inventory");
		for (var type : Type.values()) {
			inventory.then(Commands.literal(type.name().toLowerCase())
				.executes(context -> send_to_chat(context, true, type)));
			hand.then(Commands.literal(type.name().toLowerCase())
				.executes(context -> send_to_chat(context, false, type)));
		}
		var recipes = Commands.literal("recipes");
		var remove = Commands.literal("remove");
		RscRecipesRemoveCommand.register_remove(remove);
		recipes.then(remove);
		rsc.then(dump);
		rsc.then(hand);
		rsc.then(inventory);
		rsc.then(recipes);
		dispatcher.register(rsc);
	}
	private static int send_to_chat(CommandContext<CommandSourceStack> context, boolean inventory, Type type) {
		var source = context.getSource();
		var player = source.getPlayer();
		if (player != null) {
			var string = "";
			if (inventory) {
				var array = new JsonArray();
				for (var stack : player.getInventory().items) {
					var json = jsonify_stack(stack, type);
					var json_is_empty = json.isJsonObject() && json.getAsJsonObject().asMap().isEmpty();
					if (!json_is_empty && !stack.isEmpty())
						array.add(json);
				}
				string = PRETTY_GSON.toJson(array);
			} else string = PRETTY_GSON.toJson(jsonify_stack(player.getMainHandItem(), type));
			player.displayClientMessage(Component.literal(string)
					.withStyle(Style.EMPTY
						.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							Component.literal("Click to copy")))
						.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, string))),
				false);
		}
		return 0;
	}
	private static JsonElement jsonify_stack(ItemStack stack, Type type) {
		if (type == Type.TYPE)
			return new JsonPrimitive(String.valueOf(ForgeRegistries.ITEMS.getKey(stack.getItem())));
		var tag = stack.getOrCreateTag().copy();
		if (type == Type.TAG_FULL) {
			tag.putString("id", String.valueOf(ForgeRegistries.ITEMS.getKey(stack.getItem())));
			tag.putByte("Count", (byte) stack.getCount());
		}
		if (type == Type.STACK) {
			var stack_tag = new CompoundTag();
			stack_tag.putString("item", String.valueOf(ForgeRegistries.ITEMS.getKey(stack.getItem())));
			stack_tag.put("tag", tag);
			return Parsing.tag_to_json(stack_tag);
		}
		return Parsing.tag_to_json(tag);
	}
	private enum Type {
		TYPE, TAG, TAG_FULL, STACK
	}
}
