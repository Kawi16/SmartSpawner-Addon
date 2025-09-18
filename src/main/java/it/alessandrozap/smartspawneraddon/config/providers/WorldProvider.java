package it.alessandrozap.smartspawneraddon.config.providers;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import it.alessandrozap.smartspawneraddon.objects.World;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;

import java.util.HashMap;
import java.util.Map;

public class WorldProvider implements TypeProvider<World> {

    @Override
    public World load(LoadContext context) {
        if (!(context.getObject() instanceof Section)) {
            throw new IllegalArgumentException("Person must be a section");
        }

        Section section = (Section) context.getObject();
        Section breakSection = section.getSection("break");
        Section silkTouchSection = breakSection.getSection("silk-touch");
        World world = new World(section.getNameAsString());

        world.setBreakEnabled(breakSection.getBoolean("enabled"));
        world.setSilkTouchRequired(silkTouchSection.getBoolean("required"));
        world.setSilkTouchLevel(silkTouchSection.getInt("level"));
        world.setPlaceEnabled(section.getBoolean("place"));
        world.setStackEnabled(section.getBoolean("stack"));

        return world;
    }

    @Override
    public Object save(SaveContext<World> context) {
        World world = context.getObject();

        Map<String, Object> silkTouchMap = new HashMap<>();
        silkTouchMap.put("required", world.isSilkTouchRequired());
        silkTouchMap.put("level", world.getSilkTouchLevel());

        Map<String, Object> breakMap = new HashMap<>();
        breakMap.put("enabled", world.isBreakEnabled());
        breakMap.put("silk-touch", silkTouchMap);

        Map<String, Object> section = new HashMap<>();
        section.put("break", breakMap);
        section.put("place", world.isPlaceEnabled());
        section.put("stack", world.isStackEnabled());

        return section;
    }
}