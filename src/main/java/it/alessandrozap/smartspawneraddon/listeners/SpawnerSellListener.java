package it.alessandrozap.smartspawneraddon.listeners;

import github.nighter.smartspawner.api.events.SpawnerSellEvent;
import it.alessandrozap.utilsapi.managers.listeners.ListenerImpl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import static it.alessandrozap.smartspawneraddon.config.Settings.Shop;

public class SpawnerSellListener implements ListenerImpl {
    @EventHandler (priority = EventPriority.LOW)
    private void onSpawnerSell(SpawnerSellEvent e) {
        if(!Shop.Earn_Percentage.isEnabled()) return;
        double amount = e.getMoneyAmount() * Shop.Earn_Percentage.getPercentage() / 100;
        e.setMoneyAmount(amount);
    }
}
