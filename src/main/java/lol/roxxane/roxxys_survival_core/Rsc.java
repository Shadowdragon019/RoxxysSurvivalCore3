package lol.roxxane.roxxys_survival_core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import lol.roxxane.roxxys_survival_core.blocks.ModBlocks;
import lol.roxxane.roxxys_survival_core.commands.arguments.ModArgumentTypeInfos;
import lol.roxxane.roxxys_survival_core.configs.ModClientConfig;
import lol.roxxane.roxxys_survival_core.configs.ModCommonConfig;
import lol.roxxane.roxxys_survival_core.configs.ModServerConfig;
import lol.roxxane.roxxys_survival_core.items.ModItems;
import lol.roxxane.roxxys_survival_core.mob_effects.ModMobEffects;
import lol.roxxane.roxxys_survival_core.potions.ModPotions;
import lol.roxxane.roxxys_survival_core.recipes.JeiCraftingRecipe;
import lol.roxxane.roxxys_survival_core.recipes.ModRecipeSerializers;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// NOTE: "search" updates requires a world reload
// Remove sprinting but keep swimming
// Mess with food properties
// Kelp block (that smelts into a dried kelp block)
// Burn recipes (fire)
// Smelt recipes (lava)
// Recovery compass on death (on items back from grave remove it from inventory)
// Make sheep/mobs more durable
// Merge the Nether into the overworld (have it be at the very bottom. get diamonds there!)
// Deal with creepers being gone
// Remove sweet berries (or make them take way longer to grow & spread, currently they give way too much bang for buck)
// Make wolves spawn wherever farm animals spawn so you have to protect them
// Make wolves attack farm animals
// Make mobs spawn closer to the player so at night they're forced to fight/interact with them
// Custom structures~
// Remove biomes I don't want (look at snowcapped!)
// Make phantom naturally spawn at night? (I like the idea but I'm not sure if it ignores too much of the players defenses since it can just fly in if you're not completely covered. Hmm, requires no light where the player is?)
// Iron takes more T I M E to make
// Crops take time to break (hoe is efficient!)
// Copper is used instead of emeralds
// Make leaves craft into saplings
// Ores drop themselves
// Disable XP
// Make rabbits spawn everywhereh
// Make black dye obtainable
// Make cats naturally spawn
// Merge cod/salmon (I wanna merge them into one entity if possible~)
// Remove the big iron/copper ore thingies
// Jei recipe for naming items
// Prevent zombies/undead from drowning (or turning into drowned)
// Arrow signs to point the direction of out for caves
// Enemies drop iron/diamond tools but they're very damaged (this is to show "Hey! Look at these awesome & better tools!)
// Make deepslate & ores break faster (shale speed)
// Add mob spawns
// Make cave spiders not poison
// Remove baby mob enemy spawns
// Remove sticks
// Split tab config into multiple different files
// Add option to remove item from tabs by type & ignoring nbt

// I don't think mobs *have* to have drops. Their purpose is to make the player build a base.
// Make stuff drop itself without silk touch

/// TODO: Output count for {@link JeiCraftingRecipe}
@SuppressWarnings("unused")
@Mod(Rsc.ID)
public class Rsc {
    public static final String ID = "roxxys_survival_core";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Gson COMPACT_GSON = new Gson();
    public Rsc(FMLJavaModLoadingContext context) {
        ModBlocks.REGISTRY.register(context.getModEventBus());
        ModItems.REGISTRY.register(context.getModEventBus());
        ModArgumentTypeInfos.REGISTRY.register(context.getModEventBus());
        ModMobEffects.REGISTRY.register(context.getModEventBus());
        ModPotions.REGISTRY.register(context.getModEventBus());
        ModRecipeSerializers.register();
        context.registerConfig(ModConfig.Type.SERVER, ModServerConfig.SPEC);
        context.registerConfig(ModConfig.Type.COMMON, ModCommonConfig.SPEC);
        context.registerConfig(ModConfig.Type.CLIENT, ModClientConfig.SPEC);
    }
    public static void log(Object object) {
        LOGGER.info(object.toString());
    }
    public static void log(String string, Object object) {
        LOGGER.info(string, object);
    }
    public static void log(String string, Object object_a, Object object_b) {
        LOGGER.info(string, object_a, object_b);
    }
    public static void log(String string, Object... objects) {
        LOGGER.info(string, objects);
    }
    public static void warn(Object object) {
        LOGGER.warn(object.toString());
    }
    public static void warn(String string, Object object) {
        LOGGER.warn(string, object);
    }
    public static void warn(String string, Object object_a, Object object_b) {
        LOGGER.warn(string, object_a, object_b);
    }
    public static void warn(String string, Object... objects) {
        LOGGER.warn(string, objects);
    }
}