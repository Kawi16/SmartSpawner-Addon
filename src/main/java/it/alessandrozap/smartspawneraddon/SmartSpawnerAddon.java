package it.alessandrozap.smartspawneraddon;

import it.alessandrozap.smartspawneraddon.config.ConfigKeys;
import it.alessandrozap.smartspawneraddon.utils.Utils;
import it.alessandrozap.utilsapi.UtilsAPI;
import it.alessandrozap.utilsapi.defaults.DefaultMessageProviderEn;
import it.alessandrozap.utilsapi.interfaces.IMessageProvider;
import it.alessandrozap.utilsapi.logger.LogType;
import it.alessandrozap.utilsapi.logger.Logger;
import it.alessandrozap.utilsapi.managers.file.FileManager;
import it.alessandrozap.utilsapi.managers.messages.Locale;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SmartSpawnerAddon extends JavaPlugin {
    @Getter
    private static SmartSpawnerAddon instance;
    @Getter
    private FileManager configFile, messagesFile, worldsFile;
    @Getter
    private IMessageProvider messageProvider;
    @Getter
    private UtilsAPI utilsAPI;

    @Override
    public void onEnable() {
        instance = this;

        configFile = new FileManager("config", this);
        messagesFile = new FileManager("messages", this);
        worldsFile = new FileManager("worlds", this);

        ConfigKeys.init();

        Locale.setup(messagesFile);
        LocaleAddon.init();
        Locale.reload(false, true);
        try {
            utilsAPI = new UtilsAPI(this, Locale.PREFIX.getMessage(false));
            utilsAPI.init();
            utilsAPI.getCommandManager().setSendPrefix(true);
            utilsAPI.getCommandManager().setMessageProvider(new DefaultMessageProviderEn());
            messageProvider = utilsAPI.getCommandManager().getMessageProvider();
            utilsAPI.getCommandManager().registerAll(false);
            utilsAPI.getListenersManager().registerAll(false);
        } catch(Exception ex) {
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        int worldsCount = Utils.loadWorlds();
        Logger.log(String.format("Loaded %d worlds", worldsCount), LogType.INFO);
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
