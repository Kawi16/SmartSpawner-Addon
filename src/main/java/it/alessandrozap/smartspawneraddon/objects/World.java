package it.alessandrozap.smartspawneraddon.objects;

import lombok.Data;
import lombok.Getter;

import java.util.HashMap;

@Data
public class World {
    private final String worldName;
    private int silkTouchLevel;
    private boolean breakEnabled, silkTouchRequired, placeEnabled, stackEnabled;
    @Getter
    private static HashMap<String, World> worlds = new HashMap<>();

    public World(String worldName) {
        this.worldName = worldName;
        worlds.put(worldName.toLowerCase(), this);
    }

}
