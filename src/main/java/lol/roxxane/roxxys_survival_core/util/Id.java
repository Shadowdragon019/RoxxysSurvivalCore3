package lol.roxxane.roxxys_survival_core.util;

import lol.roxxane.roxxys_survival_core.Rsc;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
public class Id {
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
}
