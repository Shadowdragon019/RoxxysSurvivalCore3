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
import lol.roxxane.roxxys_survival_core.recipes.ModRecipeSerializers;
import lol.roxxane.roxxys_survival_core.recipes.ModRecipeTypes;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// NOTE: "search" updates requires a world reload
// Remove sprinting but keep swimming
// Kelp block (that smelts into a dried kelp block)
// Recovery compass on death (on items back from grave remove it from inventory)
// Merge the Nether into the overworld (have it be at the very bottom. get diamonds there!)
// Remove sweet berries (or make them take way longer to grow & spread, currently they give way too much bang for buck)
// Remove biomes I don't want (look at snowcapped!)
// Make phantom naturally spawn at night? (I like the idea but I'm not sure if it ignores too much of the players defenses since it can just fly in if you're not completely covered. Hmm, requires no light where the player is?)
// Crops take time to break (hoe is efficient!)
// Remove the big iron/copper ore thingies
// Jei recipe for naming items
// Prevent zombies/undead from drowning (or turning into drowned)
// Arrow signs to point the direction of out for caves
// Enemies drop iron/diamond tools but they're very damaged (this is to show "Hey! Look at these awesome & better tools!)
// Split tab config into multiple different files
// Add option to remove item from tabs by type & ignoring nbt

// Remove respawn totem in favor of beds. Or just give the player access to respawn totems sooner?
// I find it annoying how I'm incentivized to run around searching for a totem. Setting your respawn should be easily obtainable but not spammable. Make it craftable?
// Right click crafting table in air to open gui
// Rightclick with sawmill to open gui!
// Make horses only require armor to ride
// Disable fire tick by default
// You slowly regen health over time. Getting hurt by an enemy causes bleeding & stops the regen.

// Spiders should also burn in day
// Half mob health
// Remove attack delay
// Reduce damage pickaxes do?
// Make furnaces not reduce fuel if not smelting anything
// Reduce use of furances as much as possible
// Make path blocks able to go under any block
// Make sheep drop wool more commonly
// Make rooted dirt & hanging vines replecatable
// Make dyeing recipes consistent with input
// Merge all dyeing recipes. Make them all 8x variants

// Make ores spawn in shale too
// Replace smooth shale with cobble
// The player can mine all blocks, pickaxes just speed things up
// Add way to make fire. Just right-click with iron ingots
// Banner pattern recipes
// When animals are leaded they follow their owner
// Increase block & entity interaction range to like 5
// Add food for sprinting/jumping effects! (movement speed, jump height, block interaction range, item pickup range, fire proof, water breathing, mining speed)
// Right-click path blocks to turn them into dirt
// Podzol, grass blocks, mycelium, etc craft into dirt
// Seeds from grass & ferns!
// Path blocks drop themselves
// Make rain less common
// Merge dye stuffs?
// Add stuff I removed ike horses, pumpkins, animals, cake, etc for creativity
// Horses get effects from eaten food
// Give horse, minecarts, & boats speed buffs (most for minecarts, I want the highest effort to give the most)
// Make minecarts get launched when they go off the rails
// Make horses not throw the player off in water
// More horses let anyone ride them
// Make desert prdy
// Retexture iron horse armor to chain mail
// Make horse armor obtainable
// Make pets/tamed/named pets immortal
// Don't consume nametags
// Shift-right-click a mob with a nametag to remove its name
// Remove swimming & make maneuvering in liquids easier
// Make horse stats not random
// Add structures/features designed to inspire the player. Like a dead tree or old ruined house. Probably mostly nature stuff.
// Remove nether portal
// Make budding amethyst spawn as an ore
// Make leaves break faster (also make torches break instantly, also just make a thing to set individual block break speeds)
// Output count for JeiCraftingRecipe
// Output support for JeiMillingRecipe (is needed)
// Increase item pickup range
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
        ModRecipeTypes.REGISTRY.register(context.getModEventBus());
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