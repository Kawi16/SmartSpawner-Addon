package it.alessandrozap.smartspawneraddon.config;

import it.alessandrozap.smartspawneraddon.SmartSpawnerAddon;
import it.alessandrozap.smartspawneraddon.objects.Action;
import lombok.Getter;
import net.j4c0b3y.api.config.StaticConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Settings extends StaticConfig {

    public Settings(SmartSpawnerAddon plugin) {
        super(new File(plugin.getDataFolder(), "config.yml"), plugin.getConfigHandler());
    }

    public static class Shop {
        @Comment("Set the earn percentage of items sell in the spawners")
        public static class Earn_Percentage {
            @Getter private static boolean enabled = false;
            @Getter private static int percentage = 100;
        }
    }

    @Key("settings")
    public static class SpawnerSettings {
        @Comment("Set the spawners limit")
        @Getter
        private static HashMap<String, Integer> limits = new HashMap<>() {{
            put("default", -1);
        }};
    }

    @Comment("List of hooks available")
    public static class Hooks {
        @Comment("Hook to ExcellentEnchants")
        public static class Enchants {
            @Getter private static boolean enabled = false;
            @Getter
            private static List<String> required = new ArrayList<>(List.of(
                "SILK_SPAWNER"
            ));
        }
        @Comment("Hook to ShopGUI+")
        public static class ShopGUI {
            @Getter private static boolean enabled = false;
        }
        @Comment("Hook to EconomyShopGUIPlus")
        public static class EconomyShopGUIPlus {
            @Getter private static boolean enabled = false;
        }
        @Comment("Log with CoreProtect the spawners action")
        public static class CoreProtect {
            @Getter private static boolean enabled = false;
        }

        @Comment("Discord Hook")
        public static class Discord {
            public static class Webhooks {
                @Getter
                private static boolean enabled = false;
                @Comment("Webhook url")
                @Getter
                private static String url = "";
                @Comment({
                        "Webhook title",
                        "Use %action% as placeholder for get the action name"
                })
                @Getter
                private static String title = "Spawner %action%";
                @Getter
                private static List<String> lines = new ArrayList<>(List.of(
                        "**Player:** %player%",
                        "**Action:** %action%",
                        "**Location:** %world% %x% %y% %z%"
                ));
                @Getter
                private static HashMap<String, Action> actions = new HashMap<>() {{
                    put("break", new Action(true, "E53935"));
                    put("eggchange", new Action(true, "9C27B0"));
                    put("expclaim", new Action(true, "9C27B0"));
                    put("explode", new Action(true, "FF5722"));
                    put("place", new Action(true, "4CAF50"));
                    put("remove", new Action(true, "607D8B"));
                    put("sell", new Action(true, "9C27B0"));
                    put("stack", new Action(true, "FFC107"));
                }};
            }
        }
    }
    public static class Placeholders {
        @Comment("Force small caps usage in addon placeholders")
        @Getter
        private static boolean FORCE_SMALL_CAPS = false;
    }
}
