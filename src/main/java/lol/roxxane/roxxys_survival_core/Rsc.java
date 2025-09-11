package lol.roxxane.roxxys_survival_core;

import com.mojang.logging.LogUtils;
import lol.roxxane.roxxys_survival_core.blocks.ModBlocks;
import lol.roxxane.roxxys_survival_core.configs.ModClientConfig;
import lol.roxxane.roxxys_survival_core.configs.ModCommonConfig;
import lol.roxxane.roxxys_survival_core.configs.ModServerConfig;
import lol.roxxane.roxxys_survival_core.items.ModItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// TODO: Remove sprinting but keep swimming
// TODO: Creative tab manipulation (make sure to add a "pre" & "post" remove so you can do replace shenanigans
@SuppressWarnings("unused")
@Mod(Rsc.ID)
public class Rsc {
    public static final String ID = "roxxys_survival_core";
    public static final Logger LOGGER = LogUtils.getLogger();
    public Rsc(FMLJavaModLoadingContext context) {
        ModBlocks.register();
        ModItems.register();
        ModBlocks.REGISTRY.register(context.getModEventBus());
        ModItems.REGISTRY.register(context.getModEventBus());
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