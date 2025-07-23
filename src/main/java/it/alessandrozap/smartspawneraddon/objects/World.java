package it.alessandrozap.smartspawneraddon.objects;

import it.alessandrozap.smartspawneraddon.config.ConfigKeys;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class World {
    @Getter
    private final String worldName;
    @Getter @Setter
    private int silkTouchLevel;
    @Getter @Setter
    private boolean breakEnabled, silkTouchRequired, placeEnabled, stackEnabled;
    @Getter
    private static HashMap<String, World> worlds = new HashMap<>();

    public World(String worldName) {
        this.worldName = worldName;
        breakEnabled = ConfigKeys.getWorldKey(worldName, "break.enabled", true).getBoolean();
        silkTouchRequired = ConfigKeys.getWorldKey(worldName, "break.silk-touch.required", true).getBoolean();
        silkTouchLevel = ConfigKeys.getWorldKey(worldName, "break.silk-touch.level", 1).getInt();
        placeEnabled = breakEnabled = ConfigKeys.getWorldKey(worldName, "place", true).getBoolean();
        stackEnabled = ConfigKeys.getWorldKey(worldName, "stack", true).getBoolean();
        worlds.put(worldName.toLowerCase(), this);
    }

}
