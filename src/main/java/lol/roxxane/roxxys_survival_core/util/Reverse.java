package lol.roxxane.roxxys_survival_core.util;

import java.util.List;
import java.util.function.Consumer;

public class Reverse {
	public static <T> void reverse_loop(List<T> list, Consumer<T> consumer) {
		for (int i = list.size() - 1; i >= 0; i--)
			consumer.accept(list.get(i));
	}
}
