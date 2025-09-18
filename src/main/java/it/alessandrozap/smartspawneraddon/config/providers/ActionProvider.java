package it.alessandrozap.smartspawneraddon.config.providers;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import it.alessandrozap.smartspawneraddon.objects.Action;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;

import java.util.HashMap;
import java.util.Map;

public class ActionProvider implements TypeProvider<Action> {
    @Override
    public Action load(LoadContext context) {
        if (!(context.getObject() instanceof Section)) {
            throw new IllegalArgumentException("Action must be a section");
        }

        Section section = (Section) context.getObject();
        Action action = new Action();

        action.setEnabled(section.getBoolean("enabled"));
        action.setColor(section.getString("color").replaceAll("#", ""));

        return action;
    }

    @Override
    public Object save(SaveContext<Action> context) {
        Map<String, Object> section = new HashMap<>();
        Action actionConfig = context.getObject();

        section.put("enabled", actionConfig.isEnabled());
        section.put("color", actionConfig.getColor().replaceAll("#", ""));

        return section;
    }
}