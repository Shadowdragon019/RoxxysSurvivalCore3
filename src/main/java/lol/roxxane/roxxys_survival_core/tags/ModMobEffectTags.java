package lol.roxxane.roxxys_survival_core.tags;

import lol.roxxane.roxxys_survival_core.util.Id;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;

public class ModMobEffectTags {
	public static TagKey<MobEffect> mc(String path) {
		return create(Id.mc(path));
	}
	public static TagKey<MobEffect> forge(String path) {
		return create(Id.forge(path));
	}
	public static TagKey<MobEffect> mod(String path) {
		return create(Id.mod(path));
	}
	public static TagKey<MobEffect> create(ResourceLocation id) {
		return TagKey.create(Registries.MOB_EFFECT, id);
	}
}
