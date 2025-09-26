package lol.roxxane.roxxys_survival_core.mob_effects;

import lol.roxxane.roxxys_survival_core.Rsc;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.world.effect.MobEffectCategory.BENEFICIAL;
import static net.minecraft.world.effect.MobEffectCategory.HARMFUL;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_TOTAL;

public class ModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Rsc.ID);
	public static final RegistryObject<MobEffect> WITCH_SLOWNESS =
		register("witch_slowness", new ModMobEffect(HARMFUL, 9154528)
			.addAttributeModifier(Attributes.MOVEMENT_SPEED, "6ebb81e5-c95f-417f-82cc-c2c144467621", -0.15, MULTIPLY_TOTAL));
	public static final RegistryObject<MobEffect> WITCH_WEAKNESS =
		register("witch_weakness", new ModAttackDamageMobEffect(HARMFUL, 4738376, -2)
			.addAttributeModifier(Attributes.ATTACK_DAMAGE, "9be996c9-a2cd-45fe-b964-23cf5e6d7dad", 0, ADDITION));
	public static final RegistryObject<MobEffect> WITCH_MINING_FATIGUE =
		register("witch_mining_fatigue", new ModMobEffect(HARMFUL, 4738376)
			.addAttributeModifier(Attributes.ATTACK_SPEED, "bded7202-63da-485e-a05b-9d61f41cbbec", -.1, MULTIPLY_TOTAL));
	public static final RegistryObject<MobEffect> WITCH_SPEED =
		register("witch_speed", new ModMobEffect(BENEFICIAL, 3402751)
			.addAttributeModifier(Attributes.MOVEMENT_SPEED, "c0e86a35-f41d-4430-b793-1d36af2e6840", .2, MULTIPLY_TOTAL));
	public static final RegistryObject<MobEffect> WITCH_STRENGTH =
		register("witch_strength", new ModAttackDamageMobEffect(BENEFICIAL, 16762624, 2)
			.addAttributeModifier(Attributes.ATTACK_DAMAGE, "2e71e48a-434b-4aa1-990e-36336848588f", 0, ADDITION));
	public static final RegistryObject<MobEffect> WITCH_REGENERATION =
		register("witch_regeneration", new ModRegenerationMobEffect(BENEFICIAL, 13458603));
	public static final RegistryObject<MobEffect> WITCH_JUMP_BOOST =
		register("witch_jump_boost", new ModMobEffect(BENEFICIAL, 16646020));
	public static final RegistryObject<MobEffect> WITCH_RESISTANCE =
		register("witch_resistance", new ModMobEffect(BENEFICIAL, 9520880));
	public static final RegistryObject<MobEffect> WITCH_INVISIBILITY =
		register("witch_invisibility", new ModMobEffect(BENEFICIAL, 16185078));
	public static final RegistryObject<MobEffect> WITCH_FIRE_RESISTANCE =
		register("witch_fire_resistance", new ModMobEffect(BENEFICIAL, 16750848));
	public static final RegistryObject<MobEffect> WITCH_WATER_BREATHING =
		register("witch_water_breathing", new ModMobEffect(BENEFICIAL, 10017472));
	public static final RegistryObject<MobEffect> WITCH_INSTANT_DAMAGE =
		register("witch_instant_damage", new ModInstantMobEffect(HARMFUL, 11101546));
	public static final RegistryObject<MobEffect> WITCH_INSTANT_HEALTH =
		register("witch_instant_health", new ModInstantMobEffect(BENEFICIAL, 16262179));
	private static <T extends MobEffect> RegistryObject<T> register(String path, T effect) {
		return REGISTRY.register(path, () -> effect);
	}
}
