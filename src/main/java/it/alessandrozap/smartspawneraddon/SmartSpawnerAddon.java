package it.alessandrozap.smartspawneraddon;

import it.alessandrozap.smartspawneraddon.config.Settings;
import it.alessandrozap.smartspawneraddon.config.SettingsWorld;
import it.alessandrozap.smartspawneraddon.config.providers.ActionProvider;
import it.alessandrozap.smartspawneraddon.config.providers.WorldProvider;
import it.alessandrozap.smartspawneraddon.objects.Action;
import it.alessandrozap.smartspawneraddon.objects.World;
import it.alessandrozap.utilsapi.UtilsAPI;
import it.alessandrozap.utilsapi.logger.LogType;
import it.alessandrozap.utilsapi.logger.Logger;
import it.alessandrozap.utilsapi.managers.messages.Locale;
import lombok.Getter;
import net.j4c0b3y.api.config.platform.bukkit.BukkitConfigHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class SmartSpawnerAddon extends JavaPlugin {
    @Getter
    private static SmartSpawnerAddon instance;
    @Getter
    private BukkitConfigHandler configHandler;
    @Getter
    private Settings configSettings;
    @Getter
    private SettingsWorld settingsWorld;
    @Getter
    private LocaleAddon localeAddon;
    @Getter
    private String prefix;

    @Getter
    private UtilsAPI utilsAPI;

    @Override
    public void onEnable() {
        instance = this;

        configHandler = new BukkitConfigHandler(getLogger());
        configHandler.setFormatStructure(false);
        configHandler.setFormatValues(false);
        configHandler.setRemoveUnrecognised(false);
        configHandler.setKeyFormatter(key -> key.replace("_", "-").toLowerCase());
        configHandler.setCreateBackupOnRemove(true);
        configHandler.setBackupDateFormat("yyyy-MM-dd-hh-mm-a");

        configSettings = new Settings(this);
        localeAddon = new LocaleAddon(this);
        settingsWorld = new SettingsWorld(this);

        configHandler.bind(World.class, new WorldProvider());
        configHandler.bind(Action.class, new ActionProvider());

        configSettings.load();
        localeAddon.load();
        settingsWorld.load();

        try {
            prefix = LocaleAddon.PREFIX.map(Locale::translate).getLines().getFirst();
            if(prefix == null || prefix.isBlank()) prefix = "&#5A5A5A[&#18963Fꜱ&#199941ᴍ&#1B9C43ᴀ&#1C9F45ʀ&#1DA247ᴛ&#1FA448ꜱ&#20A74Aᴘ&#21AA4Cᴀ&#23AD4Eᴡ&#24B050ɴ&#27B251ᴇ&#2AB552ʀ&#2DB753ꜱ &#33BC55ᴀ&#36BE56ᴅ&#39C057ᴅ&#3CC358ᴏ&#3FC559ɴ&#5A5A5A] ";
            utilsAPI = new UtilsAPI(this, prefix);
            utilsAPI.init();
            utilsAPI.getCommandManager().registerAll(true);
            utilsAPI.getListenersManager().registerAll(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.log("If you see this, open an issue on GitHub - https://github.com/Kawi16/UtilsAPI", LogType.INFO);
        }

        Logger.log(String.format("Loaded %d worlds", SettingsWorld.worlds.size()), LogType.INFO);
        Logger.log("SmartSpawner Addon enabled successfully", LogType.INFO);
    }

    @Override
    public void onDisable() {
        if(utilsAPI != null && utilsAPI.isInitialized()) {
            utilsAPI.shutdown();
            utilsAPI = null;
        }
        instance = null;
        Logger.log("SmartSpawner-Addon disabled", LogType.INFO);
    }
}
