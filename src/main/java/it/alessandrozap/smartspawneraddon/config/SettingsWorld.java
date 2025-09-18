package it.alessandrozap.smartspawneraddon.config;

import it.alessandrozap.smartspawneraddon.SmartSpawnerAddon;
import it.alessandrozap.smartspawneraddon.objects.World;
import net.j4c0b3y.api.config.StaticConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SettingsWorld extends StaticConfig {
    public SettingsWorld(SmartSpawnerAddon plugin) {
        super(new File(plugin.getDataFolder(), "worlds.yml"), plugin.getConfigHandler());
    }

    @Comment({
            "World-specific settings",
            "Section example:",
            "#worlds:",
            "#  worldName:",
            "#    stack: true",
            "#    break:",
            "#      enabled: true",
            "#      silk-touch: # Require global silk touch disabled",
            "#        level: 1",
            "#        required: false",
            "#    place: true"
    })
    public static Map<String, World> worlds = new HashMap<>();
}
