/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package lol.roxxane.roxxys_survival_core.commands.arguments;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CertainEnumsArgument<T extends Enum<T>> implements ArgumentType<T> {
	private static final Dynamic2CommandExceptionType INVALID_ENUM = new Dynamic2CommandExceptionType(
		(found, constants) -> Component.translatable("commands.forge.arguments.enum.invalid", constants, found));
	private final Class<T> enum_class;
	private final List<T> allowed_enums;
	public static <R extends Enum<R>> CertainEnumsArgument<R> certain_enums_argument(Class<R> enum_class, List<R> enums) {
		return new CertainEnumsArgument<>(enum_class, enums);
	}
	public static <R extends Enum<R>> CertainEnumsArgument<R> certain_enums_argument(Class<R> enum_class) {
		return new CertainEnumsArgument<>(enum_class, Arrays.stream(enum_class.getEnumConstants()).toList());
	}
	private CertainEnumsArgument(Class<T> enum_class, List<T> allowed_enums) {
		this.enum_class = enum_class;
		this.allowed_enums = allowed_enums;
	}
	public T parse(StringReader reader) throws CommandSyntaxException {
		var name = reader.readUnquotedString();
		try {
			var found_enum = Enum.valueOf(enum_class, name.toUpperCase());
			if (allowed_enums.contains(found_enum))
				return found_enum;
			throw new IllegalArgumentException(); // This is stupid. Throwing an exception like an if-else statement. Fuck you but it works.
		} catch (IllegalArgumentException e) {
			throw INVALID_ENUM.createWithContext(reader, name,
				Arrays.toString(allowed_enums.stream().map(_enum -> _enum.name().toLowerCase()).toArray()));
		}
	}
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggest(allowed_enums.stream().map(_enum -> _enum.name().toLowerCase()), builder);
	}
	// I'm not gonna put in the effort to change this, but why does Forge do .collect instead of .toList? Does it change stuff? Is it important?
	public Collection<String> getExamples() {
		return allowed_enums.stream().map(_enum -> _enum.name().toLowerCase()).collect(Collectors.toList());
	}
	public static class Info<T extends Enum<T>> implements ArgumentTypeInfo<CertainEnumsArgument<T>, Info<T>.Template> {
		public void serializeToNetwork(Template template, FriendlyByteBuf buffer) {
			buffer.writeUtf(template.enum_class.getName());
			buffer.writeCollection(template.allowed_enums, ($, allowed_enum) -> buffer.writeUtf(allowed_enum.name().toLowerCase()));
		}
		@SuppressWarnings({"unchecked", "DataFlowIssue"})
		public Template deserializeFromNetwork(FriendlyByteBuf buffer) {
            try {
				var enum_clazz = (Class<T>) Class.forName(buffer.readUtf());
	            var allowed_enums = buffer.readCollection(ArrayList::new,
		            $ -> Enum.valueOf(enum_clazz, buffer.readUtf().toUpperCase()));
                return new Template(enum_clazz, allowed_enums);
            } catch (ClassNotFoundException e) {
                return null;
            }
		}
		public void serializeToJson(Template template, JsonObject json) {
			json.addProperty("enum", template.enum_class.getName());
			var allowed_enums_array = new JsonArray();
			template.allowed_enums.forEach(_enum -> allowed_enums_array.add(_enum.name().toLowerCase()));
			json.add("allowed", allowed_enums_array);
		}
		public Template unpack(CertainEnumsArgument<T> argument) {
			return new Template(argument.enum_class, argument.allowed_enums);
		}
		public class Template implements ArgumentTypeInfo.Template<CertainEnumsArgument<T>> {
			final Class<T> enum_class;
			final List<T> allowed_enums;
			Template(Class<T> enum_class, List<T> allowed_enums) {
				this.enum_class = enum_class;
				this.allowed_enums = allowed_enums;
			}
			public CertainEnumsArgument<T> instantiate(CommandBuildContext $) {
				return new CertainEnumsArgument<>(enum_class, allowed_enums);
			}
			public ArgumentTypeInfo<CertainEnumsArgument<T>, ?> type() {
				return Info.this;
			}
		}
	}
}
