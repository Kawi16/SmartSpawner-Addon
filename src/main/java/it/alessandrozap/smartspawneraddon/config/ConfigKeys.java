package it.alessandrozap.smartspawneraddon.config;

import it.alessandrozap.smartspawneraddon.SmartSpawnerAddon;
import it.alessandrozap.utilsapi.config.ConfigEntry;
import it.alessandrozap.utilsapi.interfaces.IConfigEntry;

public class ConfigKeys {
    public static IConfigEntry SHOP_PERCENTAGE_ENABLED;
    public static IConfigEntry SHOP_PERCENTAGE_PERCENTAGE;

    public static void init() {
        SHOP_PERCENTAGE_ENABLED = ConfigEntry.of(SmartSpawnerAddon.getInstance().getConfigFile(), "shop.earn_percentage.enabled", false);
        SHOP_PERCENTAGE_PERCENTAGE = ConfigEntry.of(SmartSpawnerAddon.getInstance().getConfigFile(), "shop.earn_percentage.percentage", 100);
    }

    public static IConfigEntry getWorldKey(String world, String key, Object defaultValue) {
        return ConfigEntry.of(SmartSpawnerAddon.getInstance().getWorldsFile(), "worlds." + world + "." + key, defaultValue);
    }
}
