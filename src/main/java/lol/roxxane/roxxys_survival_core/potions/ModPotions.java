package lol.roxxane.roxxys_survival_core.potions;

import lol.roxxane.roxxys_survival_core.Rsc;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModPotions {
	public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, Rsc.ID);
	private static RegistryObject<Potion> register(String path, Supplier<MobEffect> effect, int duration, int level) {
		return REGISTRY.register(path, () -> new Potion(new MobEffectInstance(effect.get(), duration, level)));
	}
	private static RegistryObject<Potion> register(String path, MobEffect effect, int duration, int level) {
		return REGISTRY.register(path, () -> new Potion(new MobEffectInstance(effect, duration, level)));
	}
}
