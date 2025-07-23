package it.alessandrozap.smartspawneraddon.listeners;

import github.nighter.smartspawner.api.events.SpawnerPlaceEvent;
import github.nighter.smartspawner.api.events.SpawnerPlayerBreakEvent;
import github.nighter.smartspawner.api.events.SpawnerStackEvent;
import it.alessandrozap.smartspawneraddon.objects.World;
import it.alessandrozap.utilsapi.managers.listeners.ListenerImpl;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;

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

    @EventHandler
    public void onSpawnerPlace(SpawnerPlaceEvent e) {
        String worldName = e.getLocation().getWorld().getName().toLowerCase();
        if(!World.getWorlds().containsKey(worldName)) return;
        World world = World.getWorlds().get(worldName);
        if(world.isPlaceEnabled()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onSpawnerStack(SpawnerStackEvent e) {
        String worldName = e.getLocation().getWorld().getName().toLowerCase();
        if(!World.getWorlds().containsKey(worldName)) return;
        World world = World.getWorlds().get(worldName);
        if(world.isStackEnabled()) return;
        e.setCancelled(true);
    }
}
