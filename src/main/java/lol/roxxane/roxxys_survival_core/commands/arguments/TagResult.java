package lol.roxxane.roxxys_survival_core.commands.arguments;

import com.mojang.datafixers.util.Either;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument.Result;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.Optional;

public record TagResult<T>(TagKey<T> key) implements Result<T> {
	public Either<ResourceKey<T>, TagKey<T>> unwrap() {
		return Either.right(key);
	}
	public <E> Optional<Result<E>> cast(ResourceKey<? extends Registry<E>> registry_key) {
		return key.cast(registry_key).map(TagResult::new);
	}
	public boolean test(Holder<T> pHolder) {
		return pHolder.is(key);
	}
	public String asPrintable() {
		return "#" + key.location();
	}
}