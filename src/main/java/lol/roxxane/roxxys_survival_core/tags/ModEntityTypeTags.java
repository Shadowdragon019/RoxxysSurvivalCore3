package lol.roxxane.roxxys_survival_core.tags;

import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ModEntityTypeTags {
	public static TagKey<EntityType<?>> mc(String path) {
		return create(Id.mc(path));
	}
	public static TagKey<EntityType<?>> forge(String path) {
		return create(Id.forge(path));
	}
	public static TagKey<EntityType<?>> mod(String path) {
		return create(Id.mod(path));
	}
	public static TagKey<EntityType<?>> create(ResourceLocation id) {
		return TagKey.create(Registries.ENTITY_TYPE, id);
	}
}
