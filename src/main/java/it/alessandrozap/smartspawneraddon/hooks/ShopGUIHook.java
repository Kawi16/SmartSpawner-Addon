package it.alessandrozap.smartspawneraddon.hooks;

import github.nighter.smartspawner.api.events.SpawnerSellEvent;
import it.alessandrozap.smartspawneraddon.config.Settings;
import it.alessandrozap.utilsapi.managers.listeners.ListenerImpl;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.modifier.PriceModifier;
import net.brcdev.shopgui.modifier.PriceModifierActionType;
import net.brcdev.shopgui.shop.item.ShopItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

public class ShopGUIHook implements ListenerImpl {
    @Override
    public String[] dependencies() {
        return new String[] {"ShopGUIPlus"};
    }
    @EventHandler (priority = EventPriority.LOWEST)
    private void onSpawnerSell(SpawnerSellEvent e) {
        if(!Settings.Hooks.ShopGUI.isEnabled()) return;
        try {
            double total = 0.0;
            for(ItemStack iS : e.getItems()) {
                ShopItem shopItem = ShopGuiPlusApi.getItemStackShopItem(iS);
                int amount = iS.getAmount();
                double sellPrice = shopItem.getSellPriceForAmount(amount);
                PriceModifier priceModifier = ShopGuiPlusApi.getPriceModifier(e.getPlayer(), shopItem, PriceModifierActionType.SELL);
                total += sellPrice * priceModifier.getModifier();
            }
            e.setMoneyAmount(total);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
