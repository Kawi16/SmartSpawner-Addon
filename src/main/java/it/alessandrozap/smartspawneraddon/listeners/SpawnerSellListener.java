package it.alessandrozap.smartspawneraddon.listeners;

import github.nighter.smartspawner.api.events.SpawnerSellEvent;
import it.alessandrozap.smartspawneraddon.config.ConfigKeys;
import it.alessandrozap.utilsapi.managers.listeners.ListenerImpl;
import org.bukkit.event.EventHandler;

public class SpawnerSellListener implements ListenerImpl {
    @EventHandler
    private void onSpawnerSell(SpawnerSellEvent e) {
        if(!ConfigKeys.SHOP_PERCENTAGE_ENABLED.getBoolean()) return;
        double amount = e.getMoneyAmount() * ConfigKeys.SHOP_PERCENTAGE_PERCENTAGE.getInt() / 100;
        e.setMoneyAmount(amount);
    }
}
