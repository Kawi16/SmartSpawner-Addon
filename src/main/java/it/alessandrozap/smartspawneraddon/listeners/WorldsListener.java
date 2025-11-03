package it.alessandrozap.smartspawneraddon.listeners;

import github.nighter.smartspawner.api.events.SpawnerPlaceEvent;
import github.nighter.smartspawner.api.events.SpawnerPlayerBreakEvent;
import github.nighter.smartspawner.api.events.SpawnerStackEvent;
import it.alessandrozap.smartspawneraddon.LocaleAddon;
import it.alessandrozap.smartspawneraddon.config.Settings;
import it.alessandrozap.smartspawneraddon.objects.World;
import it.alessandrozap.utilsapi.managers.listeners.ListenerImpl;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.HashMap;
import java.util.Map;

public class WorldsListener implements ListenerImpl {
    @EventHandler
    public void onSpawnerBreak(SpawnerPlayerBreakEvent e) {
        String worldName = e.getLocation().getWorld().getName().toLowerCase();
        if(!World.getWorlds().containsKey(worldName)) return;
        World world = World.getWorlds().get(worldName);
        if(!world.isBreakEnabled()) {
            e.setCancelled(true);
            return;
        }
        if(world.isSilkTouchRequired()) {
            int level = e.getPlayer().getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.SILK_TOUCH);
            if(level == 0 || level < world.getSilkTouchLevel()) e.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onSpawnerPlace(SpawnerPlaceEvent e) {
        Player player = e.getPlayer();
        int maxSize = checkStack(player);
        if(maxSize != -1 && maxSize < e.getQuantity()) {
            e.setCancelled(true);
            final int finalMaxSize = maxSize;
            LocaleAddon.sendMessage(player, LocaleAddon.TOO_MANY_SPAWNERS, new HashMap<>() {{
                put("%number%", String.valueOf(finalMaxSize));
            }});
            return;
        }
        String worldName = e.getLocation().getWorld().getName().toLowerCase();
        if(!World.getWorlds().containsKey(worldName)) return;
        World world = World.getWorlds().get(worldName);
        if(world.isPlaceEnabled()) return;
        e.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onSpawnerStack(SpawnerStackEvent e) {
        Player player = e.getPlayer();
        int maxSize = checkStack(player);
        if(maxSize != -1 && maxSize < e.getNewQuantity()) {
            e.setCancelled(true);
            final int finalMaxSize = maxSize;
            LocaleAddon.sendMessage(player, LocaleAddon.TOO_MANY_SPAWNERS, new HashMap<>() {{
                put("%number%", String.valueOf(finalMaxSize));
            }});
            return;
        }
        String worldName = e.getLocation().getWorld().getName().toLowerCase();
        if(!World.getWorlds().containsKey(worldName)) return;
        World world = World.getWorlds().get(worldName);
        if(world.isStackEnabled()) return;
        e.setCancelled(true);
    }

    private int checkStack(Player player) {
        if(!player.hasPermission("smartspawner-addon.stack.bypass") && !player.hasPermission("smartspawner-addon.admin")) {
            int maxSize = -1;
            for (Map.Entry<String, Integer> entry : Settings.SpawnerSettings.getLimits().entrySet()) {
                if(!player.hasPermission("smartspawner-addon.stack." + entry.getKey())) continue;
                if(entry.getValue() > maxSize) maxSize = entry.getValue();
            }
            return maxSize;
        }
        return -1;
    }
}
