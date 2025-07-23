package it.alessandrozap.smartspawneraddon.utils;

import it.alessandrozap.smartspawneraddon.SmartSpawnerAddon;
import it.alessandrozap.smartspawneraddon.objects.World;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class Utils {
    public static int loadWorlds() {
        World.getWorlds().clear();
        ConfigurationSection worldsSection = SmartSpawnerAddon.getInstance().getWorldsFile().getConfig().getConfigurationSection("worlds");
        int count = 0;
        if(worldsSection == null) return count;
        for(String key : worldsSection.getKeys(false)) {
            if(!worldsSection.isConfigurationSection(key)) continue;
            ConfigurationSection worldSection = worldsSection.getConfigurationSection(key);
            if(worldSection == null) continue;
            if(Bukkit.getWorld(key) == null) continue;
            new World(key);
            count++;
        }
        return count;
    }
}
