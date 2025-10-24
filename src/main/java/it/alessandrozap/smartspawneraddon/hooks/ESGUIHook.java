package it.alessandrozap.smartspawneraddon.hooks;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.api.events.SpawnerSellEvent;
import it.alessandrozap.smartspawneraddon.config.Settings;
import me.gypopo.economyshopgui.api.EconomyShopGUIHook;
import me.gypopo.economyshopgui.api.prices.AdvancedSellPrice;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.util.EconomyType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ESGUIHook implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSell(SpawnerSellEvent e) {
        if(!Settings.Hooks.EconomyShopGUIPlus.isEnabled()) return;
        HashMap<EconomyType, Double> prices = new HashMap<>();
        Map<ShopItem, Integer> soldItems = new HashMap<>();
        int totalAmount = 0;

        for (ItemStack item : e.getItems()) {
            ShopItem shopItem = EconomyShopGUIHook.getShopItem(item);
            if (shopItem == null) continue;
            if (!EconomyShopGUIHook.isSellAble(shopItem)) continue;

            int limit = getSellLimit(shopItem, e.getPlayer().getUniqueId(), item.getAmount());
            if (limit == -1) continue;

            limit = getMaxSell(shopItem, limit, soldItems.getOrDefault(shopItem, 0));
            if (limit == -1) continue;

            if (EconomyShopGUIHook.hasMultipleSellPrices(shopItem)) {
                AdvancedSellPrice sellPrice = EconomyShopGUIHook.getMultipleSellPrices(shopItem);
                sellPrice.getSellPrices(
                        sellPrice.giveAll() ? null : sellPrice.getSellTypes().get(0),
                        e.getPlayer(),
                        item,
                        limit,
                        totalAmount
                ).forEach((type, price) -> {
                    prices.put(type.getType(), prices.getOrDefault(type, 0d) + price);
                });
            } else {
                double sellPrice = EconomyShopGUIHook.getItemSellPrice(shopItem, item, e.getPlayer(), limit, totalAmount);
                prices.put(shopItem.getEcoType().getType(), prices.getOrDefault(shopItem.getEcoType().getType(), 0d) + sellPrice);
            }
            totalAmount += limit;
            soldItems.put(shopItem, soldItems.getOrDefault(shopItem, 0) + limit);
        }

        String s = SmartSpawner.getInstance().getItemPriceManager().getCurrencyManager().getActiveCurrencyProvider();
        EconomyType type = s.equalsIgnoreCase("COINSENGINE") ? EconomyType.COINS_ENGINE : EconomyType.VAULT;
        e.setMoneyAmount(prices.getOrDefault(type, e.getMoneyAmount()));
    }

    private int getSellLimit(ShopItem shopItem, UUID playerUUID, int amount) {
        if (shopItem.getLimitedSellMode() != 0) {
            int stock = EconomyShopGUIHook.getSellLimit(shopItem, playerUUID);
            if (stock <= 0) return -1;
            else if (stock < amount) amount = stock;
        }
        return amount;
    }

    private int getMaxSell(ShopItem shopItem, int quantity, int sold) {
        if (shopItem.isMaxSell(sold + quantity)) {
            if (sold >= shopItem.getMaxSell()) return -1;
            quantity = shopItem.getMaxSell() - sold;
        }
        return quantity;
    }
}
