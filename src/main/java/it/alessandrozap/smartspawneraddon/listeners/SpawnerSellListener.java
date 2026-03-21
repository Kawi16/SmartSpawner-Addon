package it.alessandrozap.smartspawneraddon.listeners;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.api.events.SpawnerSellEvent;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import it.alessandrozap.smartspawneraddon.objects.SpawnerMultiplier;
import it.alessandrozap.utilsapi.managers.listeners.ListenerImpl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import static it.alessandrozap.smartspawneraddon.config.Settings.Shop;

public class SpawnerSellListener implements ListenerImpl {
    @EventHandler (priority = EventPriority.LOW)
    private void onSpawnerSell(SpawnerSellEvent e) {
        if(!Shop.Earn_Percentage.isEnabled()) return;
        double amount = e.getMoneyAmount();
        if (!Shop.Earn_Percentage.isPerSpawnerSize()) amount = amount * Shop.Earn_Percentage.getPercentage() / 100;
        else {
            SpawnerData data = SmartSpawner.getInstance().getSpawnerManager().getSpawnerByLocation(e.getLocation());
            if (data == null) return;
            int multiplier = SpawnerMultiplier.getMultiplier(data.getEntityType().name(), data.getStackSize());
            int multiplierAll = SpawnerMultiplier.getMultiplier("ALL", data.getStackSize());
            if (multiplier != -1) amount = amount * multiplier / 100;
            else if (multiplierAll != -1) amount = amount * multiplierAll / 100;
        }
        e.setMoneyAmount(amount);
    }
}
