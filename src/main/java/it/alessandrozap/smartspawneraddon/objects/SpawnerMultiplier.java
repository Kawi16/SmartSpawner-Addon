package it.alessandrozap.smartspawneraddon.objects;

import lombok.Getter;

import java.util.HashMap;

public class SpawnerMultiplier {
    @Getter
    private String type;
    @Getter
    private int multiplier, stackSize;
    @Getter
    private static HashMap<String, HashMap<Integer, SpawnerMultiplier>> multipliers = new HashMap<>();

    public SpawnerMultiplier(String type, int multiplier, int stackSize) {
        this.type = type.toUpperCase();
        this.multiplier = multiplier;
        this.stackSize = stackSize;
        if (!multipliers.containsKey(type)) {
            HashMap<Integer, SpawnerMultiplier> map = new HashMap<>();
            map.put(stackSize, this);
            multipliers.put(type, map);
        } else multipliers.get(type).put(stackSize, this);
    }

    public static int getMultiplier(String entityType, int stackSize) {
        if (!multipliers.containsKey(entityType)) return -1;
        HashMap<Integer, SpawnerMultiplier> temp = multipliers.get(entityType);
        if (!temp.containsKey(stackSize)) return -1;
        SpawnerMultiplier spawnerMultiplier = temp.get(stackSize);
        return spawnerMultiplier.getMultiplier();
    }
}
