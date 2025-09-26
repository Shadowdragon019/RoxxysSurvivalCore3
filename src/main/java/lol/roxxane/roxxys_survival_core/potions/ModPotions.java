package lol.roxxane.roxxys_survival_core.potions;

import lol.roxxane.roxxys_survival_core.Rsc;
import lol.roxxane.roxxys_survival_core.mob_effects.ModMobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static net.minecraft.world.effect.MobEffects.*;

public class ModPotions {
	public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, Rsc.ID);
	public static final RegistryObject<Potion> WITCH_SLOWNESS = register("witch_slowness", ModMobEffects.WITCH_SLOWNESS, 400, 0);
	public static final RegistryObject<Potion> WITCH_WEAKNESS = register("witch_weakness", ModMobEffects.WITCH_WEAKNESS, 400, 0);
	public static final RegistryObject<Potion> WITCH_MINING_FATIGUE = register("witch_mining_fatigue", ModMobEffects.WITCH_MINING_FATIGUE, 400, 0);
	// Doesn't need its own mob effect as it already only affect players
	public static final RegistryObject<Potion> WITCH_BLINDNESS = register("witch_blindness", BLINDNESS, 300, 0);
	public static final RegistryObject<Potion> WITCH_SPEED = register("witch_speed", ModMobEffects.WITCH_SPEED, 1200, 0);
	public static final RegistryObject<Potion> WITCH_STRENGTH = register("witch_strength", ModMobEffects.WITCH_STRENGTH, 1200, 0);
	public static final RegistryObject<Potion> WITCH_REGENERATION = register("witch_regeneration", ModMobEffects.WITCH_REGENERATION, 1200, 0);
	public static final RegistryObject<Potion> WITCH_JUMP_BOOST = register("witch_jump_boost", ModMobEffects.WITCH_JUMP_BOOST, 1200, 0);
	public static final RegistryObject<Potion> WITCH_RESISTANCE = register("witch_resistance", ModMobEffects.WITCH_RESISTANCE, 1200, 0);
	public static final RegistryObject<Potion> WITCH_INVISIBILITY = register("witch_invisibility", ModMobEffects.WITCH_INVISIBILITY, 1200, 0);
	public static final RegistryObject<Potion> WITCH_FIRE_RESISTANCE = register("witch_fire_resistance", ModMobEffects.WITCH_FIRE_RESISTANCE, 1200, 0);
	public static final RegistryObject<Potion> WITCH_WATER_BREATHING = register("witch_water_breathing", ModMobEffects.WITCH_WATER_BREATHING, 1200, 0);
	public static final RegistryObject<Potion> WITCH_INSTANT_DAMAGE = register("witch_instant_damage", ModMobEffects.WITCH_INSTANT_DAMAGE, 1, 0);
	public static final RegistryObject<Potion> WITCH_INSTANT_HEALTH = register("witch_instant_health", ModMobEffects.WITCH_INSTANT_HEALTH, 1, 0);
	private static RegistryObject<Potion> register(String path, Supplier<MobEffect> effect, int duration, int level) {
		return REGISTRY.register(path, () -> new Potion(new MobEffectInstance(effect.get(), duration, level)));
	}
	private static RegistryObject<Potion> register(String path, MobEffect effect, int duration, int level) {
		return REGISTRY.register(path, () -> new Potion(new MobEffectInstance(effect, duration, level)));
	}
}
