package lol.roxxane.roxxys_survival_core.commands.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import lol.roxxane.roxxys_survival_core.Rsc;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfo.Template;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static net.minecraft.commands.synchronization.ArgumentTypeInfos.registerByClass;
import static net.minecraft.commands.synchronization.SingletonArgumentInfo.contextFree;

public class ModArgumentTypeInfos {
	public static final DeferredRegister<ArgumentTypeInfo<?, ?>> REGISTRY = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, Rsc.ID);
	public static final RegistryObject<SingletonArgumentInfo<NamespaceArgument>> NAMESPACE =
		register_singleton("namespace", NamespaceArgument.class, NamespaceArgument::namespace);
	public static final RegistryObject<SingletonArgumentInfo<PathArgument>> PATH =
		register_singleton("path", PathArgument.class, PathArgument::path);
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static final RegistryObject<CertainEnumsArgument.Info> CERTAIN_ENUMS =
		register("certain_enums", CertainEnumsArgument.class, new CertainEnumsArgument.Info());
	private static <A extends ArgumentType<?>, T extends Template<A>, I extends ArgumentTypeInfo<A, T>>
	RegistryObject<I> register(String path, Class<A> clazz, I info) {
		return REGISTRY.register(path, () -> registerByClass(clazz, info));
	}
	private static <T extends ArgumentType<?>> RegistryObject<SingletonArgumentInfo<T>> register_singleton(String path, Class<T> clazz, Supplier<T> supplier) {
		return REGISTRY.register(path, () -> registerByClass(clazz, contextFree(supplier)));
	}
}
