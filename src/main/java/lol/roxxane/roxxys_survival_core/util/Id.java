package lol.roxxane.roxxys_survival_core.util;

import lol.roxxane.roxxys_survival_core.Rsc;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
public class Id {
	public static final ResourceLocation EMPTY = of("", "");
	public static boolean is(String id) {
		return ResourceLocation.isValidResourceLocation(id);
	}
	public static ResourceLocation of(String namespace, String path) {
		return ResourceLocation.fromNamespaceAndPath(namespace, path);
	}
	public static ResourceLocation of(String id) {
		return ResourceLocation.parse(id);
	}
	public static ResourceLocation mc(String path) {
		return of("minecraft", path);
	}
	public static ResourceLocation forge(String path) {
		return of("forge", path);
	}
	public static ResourceLocation mod(String path) {
		return of(Rsc.ID, path);
	}
	public static ResourceLocation append(ResourceLocation id, String path_end) {
		return Id.of(id.getNamespace(), id.getPath() + path_end);
	}
	public static boolean is_empty(ResourceLocation id) {
		return id.getNamespace().isEmpty() || id.getPath().isEmpty();
	}
	public static boolean is_empty_or_air(ResourceLocation id) {
		return is_empty(id) || id.equals(of("air"));
	}
}
