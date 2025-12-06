package me.doozyz.resonance.support;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;


public class ModRef {

    public static final String MODID = "resonance";
    private static final Logger LOGGER = LogUtils.getLogger();

    //================= Resource Location Utility =================//
    public static ResourceLocation res(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }

    //================= Logging Utilities =================//
    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void info(String format, Object arg) {
        LOGGER.info(format, arg);
    }

    public static void info(String format, Object... args) {
        LOGGER.info(format, args);
    }

    public static void debug(String message) {
        LOGGER.debug(message);
    }

    public static void debug(String format, Object arg) {
        LOGGER.debug(format, arg);
    }

    public static void debug(String format, Object... args) {
        LOGGER.debug(format, args);
    }

    public static void warn(String message) {
        LOGGER.warn(message);
    }

    public static void warn(String format, Object arg) {
        LOGGER.warn(format, arg);
    }

    public static void warn(String format, Object... args) {
        LOGGER.warn(format, args);
    }

    public static void error(String message) {
        LOGGER.error(message);
    }

    public static void error(String format, Object arg) {
        LOGGER.error(format, arg);
        }
}
