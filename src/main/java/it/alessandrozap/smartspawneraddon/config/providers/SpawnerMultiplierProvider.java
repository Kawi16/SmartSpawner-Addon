package it.alessandrozap.smartspawneraddon.config.providers;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import it.alessandrozap.smartspawneraddon.objects.SpawnerMultiplier;
import it.alessandrozap.utilsapi.logger.LogType;
import it.alessandrozap.utilsapi.logger.Logger;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SpawnerMultiplierProvider implements TypeProvider<SpawnerMultiplier> {
    @Override
    public SpawnerMultiplier load(LoadContext context) {
        if (!(context.getObject() instanceof Section)) {
            throw new IllegalArgumentException("SpawnerMultiplier must be a section");
        }

        Section section = (Section) context.getObject();
        String type = section.getString("entityType");
        if (!Objects.equals(type, "ALL")) {
            try {
                EntityType.valueOf(type);
            } catch(IllegalArgumentException e) {
                Logger.log("Not valid EntityType in " + section.getRouteAsString(), LogType.ERROR);
                return null;
            }
        }
        return new SpawnerMultiplier(type, section.getInt("multiplier"), section.getInt("stackSize"));
    }

    @Override
    public Object save(SaveContext<SpawnerMultiplier> context) {
        Map<String, Object> section = new HashMap<>();
        SpawnerMultiplier multiplier = context.getObject();

        section.put("entityType", multiplier.getType());
        section.put("multiplier", multiplier.getMultiplier());
        section.put("stackSize", multiplier.getStackSize());
        return section;
    }
}