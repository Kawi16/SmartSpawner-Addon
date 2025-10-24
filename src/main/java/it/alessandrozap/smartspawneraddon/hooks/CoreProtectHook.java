package it.alessandrozap.smartspawneraddon.hooks;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.api.events.*;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import it.alessandrozap.smartspawneraddon.config.Settings;
import it.alessandrozap.smartspawneraddon.discord.DiscordWebhook;
import it.alessandrozap.smartspawneraddon.objects.ActionType;
import it.alessandrozap.utilsapi.managers.listeners.ListenerImpl;
import net.coreprotect.CoreProtect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class CoreProtectHook implements ListenerImpl {
    @Override
    public String[] dependencies() {
        return new String[] {"CoreProtect"};
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpawnPlace(SpawnerPlaceEvent e) {
        if(!check(e)) return;
        Location loc = e.getLocation();
        BlockData blockData = loc.getBlock().getBlockData();
        CoreProtect.getInstance().getAPI().logPlacement(
                e.getPlayer().getName() + " (" + e.getEntityType().name() + " [" + e.getQuantity() +"])",
                loc,
                loc.getBlock().getType(),
                blockData);
        logAction(e.getPlayer(), e.getLocation(), e.getQuantity(), ActionType.PLACE);
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onSpawnerBreak(SpawnerPlayerBreakEvent e) {
        if(!check(e)) return;
        logAction(e.getPlayer(), e.getLocation(), e.getQuantity(), ActionType.REMOVE);
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onSpawnerStack(SpawnerStackEvent e) {
        if(!check(e)) return;
        logAction(e.getPlayer(), e.getLocation(), e.getNewQuantity() - e.getOldQuantity(), ActionType.STACK);
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onSpawnerRemove(SpawnerRemoveEvent e) {
        if(!check(e)) return;
        logAction(e.getPlayer(), e.getLocation(), e.getChangeAmount(), ActionType.REMOVE);
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onSpawnerEggChange(SpawnerEggChangeEvent e) {
        if(!check(e)) return;
        logChange(e.getPlayer(), e.getLocation(), e.getOldEntityType(), e.getNewEntityType());
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onSpawnerExplode(SpawnerExplodeEvent e) {
        DiscordWebhook.sendEmbed(null, e.getLocation(), ActionType.EXPLODE);
    }

    @EventHandler
    public void onExpClaim(SpawnerExpClaimEvent e) {
        DiscordWebhook.sendEmbed(e.getPlayer(), e.getLocation(), ActionType.EXPCLAIM);
    }

    private void logAction(Player player, Location location, int quantity, ActionType type) {
        SpawnerData data = SmartSpawner.getInstance().getSpawnerManager().getSpawnerByLocation(location);
        Block block = location.getBlock();

        String actor;
        if (data == null) actor = player.getName();
        else {
            actor = String.format("%s (%s [%d])",
                    player.getName(),
                    data.getEntityType().toString(),
                    quantity
            );
        }

        switch (type) {
            case PLACE:
                CoreProtect.getInstance().getAPI().logPlacement(actor, block.getState());
                break;
            case REMOVE:
                CoreProtect.getInstance().getAPI().logRemoval(actor, block.getState());
                break;
        }
        DiscordWebhook.sendEmbed(player, location, type);
    }

    private void logChange(Player player, Location location, EntityType oldEntityType, EntityType newEntityType) {
        SpawnerData data = SmartSpawner.getInstance().getSpawnerManager().getSpawnerByLocation(location);
        if(data == null) {
            CoreProtect.getInstance().getAPI().logInteraction(
                    player.getName(),
                    location
            );
            return;
        }
        String message = String.format("%s (%s -> %s)",
                player.getName(),
                oldEntityType,
                newEntityType
        );
        CoreProtect.getInstance().getAPI().logInteraction(
                message,
                location
        );
        DiscordWebhook.sendEmbed(player, location, ActionType.EGGCHANGE);
    }

    private <T extends Event & Cancellable> boolean check(T e) {
        if (!CoreProtect.getInstance().isEnabled() || !Settings.Hooks.CoreProtect.isEnabled()) return false;
        return !e.isCancelled();
    }
}
