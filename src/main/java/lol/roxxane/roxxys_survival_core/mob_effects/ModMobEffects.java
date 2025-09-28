package lol.roxxane.roxxys_survival_core.mob_effects;

import lol.roxxane.roxxys_survival_core.Rsc;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Rsc.ID);
	private static <T extends MobEffect> RegistryObject<T> register(String path, T effect) {
		return REGISTRY.register(path, () -> effect);
	}
}
