package it.alessandrozap.smartspawneraddon.commands;

import it.alessandrozap.smartspawneraddon.LocaleAddon;
import it.alessandrozap.smartspawneraddon.SmartSpawnerAddon;
import it.alessandrozap.smartspawneraddon.utils.Utils;
import it.alessandrozap.utilsapi.annotations.commands.Command;
import it.alessandrozap.utilsapi.annotations.commands.MainCommand;
import it.alessandrozap.utilsapi.annotations.commands.SubCommand;
import it.alessandrozap.utilsapi.managers.messages.Locale;
import org.bukkit.command.CommandSender;

@Command(name = "smartspawner-addon",
        description = "Main command for SmartSpawner Addon",
        aliases = {"ss-addon"},
        permission = "smartspawner-addon.command.main")
public class AddonCommand {
    @MainCommand(description = "Command for see general help", allowConsole = true)
    public void onRootCommand(CommandSender sender) {
        sender.sendMessage(SmartSpawnerAddon.getInstance().getUtilsAPI().getCommandManager().getHelpMessageForCommand("smartspawner-addon", sender, true).toArray(new String[0]));
    }

    @SubCommand(name = "reload",
            description = "Command for reload plugin files",
            usage = "smartspawner-addon reload",
            permission = "smartspawner-addon.command.reload",
            allowConsole = true)
    public void onReload(CommandSender sender, String[] args) {
        SmartSpawnerAddon.getInstance().getConfigFile().reload();
        SmartSpawnerAddon.getInstance().getWorldsFile().reload();
        Utils.loadWorlds();
        LocaleAddon.RELOADED_CONFIRM.send(sender, true);
    }
}
