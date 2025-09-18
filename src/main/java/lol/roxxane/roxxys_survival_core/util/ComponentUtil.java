package lol.roxxane.roxxys_survival_core.util;

import net.minecraft.network.chat.Component;

import java.util.Optional;

public class ComponentUtil {
	public static String componentToString(Component component) {
		var stringBuilder = new StringBuilder();
		component.visit(content -> {
			stringBuilder.append(content);
			return Optional.empty();
		});
		return stringBuilder.toString();
	}
}
