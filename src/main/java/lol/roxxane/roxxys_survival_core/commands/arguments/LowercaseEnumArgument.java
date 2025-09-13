/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package lol.roxxane.roxxys_survival_core.commands.arguments;

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

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LowercaseEnumArgument<T extends Enum<T>> implements ArgumentType<T> {
    private static final Dynamic2CommandExceptionType INVALID_ENUM = new Dynamic2CommandExceptionType(
        (found, constants) -> Component.translatable("commands.forge.arguments.enum.invalid", constants, found));
    private final Class<T> enum_class;
    public static <R extends Enum<R>> LowercaseEnumArgument<R> lowercase_enum_argument(Class<R> enum_class) {
        return new LowercaseEnumArgument<>(enum_class);
    }
    private LowercaseEnumArgument(final Class<T> enum_class) {
        this.enum_class = enum_class;
    }
    public T parse(final StringReader reader) throws CommandSyntaxException {
        var name = reader.readUnquotedString();
        try {
            return Enum.valueOf(enum_class, name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw INVALID_ENUM.createWithContext(reader, name, Arrays.toString(Arrays.stream(enum_class.getEnumConstants()).map(_enum -> _enum.name().toLowerCase()).toArray()));
        }
    }
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(Stream.of(enum_class.getEnumConstants()).map(_enum -> _enum.name().toLowerCase()), builder);
    }
    public Collection<String> getExamples() {
        return Stream.of(enum_class.getEnumConstants()).map(_enum -> _enum.name().toLowerCase()).collect(Collectors.toList());
    }
    public static class Info<T extends Enum<T>> implements ArgumentTypeInfo<LowercaseEnumArgument<T>, Info<T>.Template> {
        public void serializeToNetwork(Template template, FriendlyByteBuf buffer) {
            buffer.writeUtf(template.enum_class.getName());
        }
        @SuppressWarnings({"unchecked", "DataFlowIssue"})
        public Template deserializeFromNetwork(FriendlyByteBuf buffer) {
            try {
                var name = buffer.readUtf();
                return new Template((Class<T>) Class.forName(name.toUpperCase()));
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
        public void serializeToJson(Template template, JsonObject json) {
            json.addProperty("enum", template.enum_class.getName());
        }
        public Template unpack(LowercaseEnumArgument<T> argument) {
            return new Template(argument.enum_class);
        }
        public class Template implements ArgumentTypeInfo.Template<LowercaseEnumArgument<T>> {
            final Class<T> enum_class;
            Template(Class<T> enum_class)
            {
                this.enum_class = enum_class;
            }
            public LowercaseEnumArgument<T> instantiate(CommandBuildContext pStructure) {
                return new LowercaseEnumArgument<>(this.enum_class);
            }
            public ArgumentTypeInfo<LowercaseEnumArgument<T>, ?> type()
            {
                return Info.this;
            }
        }
    }
}
