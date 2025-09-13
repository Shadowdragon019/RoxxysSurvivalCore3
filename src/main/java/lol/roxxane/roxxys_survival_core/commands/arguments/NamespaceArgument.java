package lol.roxxane.roxxys_survival_core.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.ResourceLocationException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;

import static net.minecraft.resources.ResourceLocation.validNamespaceChar;

public class NamespaceArgument implements ArgumentType<String> {
	public static final SimpleCommandExceptionType ERROR = new SimpleCommandExceptionType(Component.literal("FAILED TO PARSE NAMESPACE"));
	private static final Collection<String> EXAMPLES = Arrays.asList("foo", "bar", "012");
	public static NamespaceArgument namespace() {
		return new NamespaceArgument();
	}
	public static String get_namespace(CommandContext<CommandSourceStack> context, String name) {
		return context.getArgument(name, String.class);
	}
	public String parse(StringReader reader) throws CommandSyntaxException {
		var i = reader.getCursor();
		while(reader.canRead() && validNamespaceChar(reader.peek()))
			reader.skip();
		var s = reader.getString().substring(i, reader.getCursor());
		try {
			return s;
		} catch (ResourceLocationException resourcelocationexception) {
			reader.setCursor(i);
			throw ERROR.createWithContext(reader);
		}
	}
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
