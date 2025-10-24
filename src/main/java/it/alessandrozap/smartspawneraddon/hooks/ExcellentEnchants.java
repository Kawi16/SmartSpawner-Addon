package it.alessandrozap.smartspawneraddon.hooks;

import github.nighter.smartspawner.api.events.SpawnerPlayerBreakEvent;
import it.alessandrozap.smartspawneraddon.LocaleAddon;
import it.alessandrozap.smartspawneraddon.config.Settings;
import it.alessandrozap.utilsapi.managers.listeners.ListenerImpl;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import su.nightexpress.excellentenchants.api.EnchantRegistry;
import su.nightexpress.excellentenchants.api.enchantment.CustomEnchantment;

import java.util.HashMap;
import java.util.List;

public class ExcellentEnchants implements ListenerImpl {
    @Override
    public String[] dependencies() {
        return new String[] {"ExcellentEnchants"};
    }

    @EventHandler
    public void onBreak(SpawnerPlayerBreakEvent e) {
        if(!Settings.Hooks.Enchants.isEnabled()) return;
        boolean b = true;
        List<String> list = Settings.Hooks.Enchants.getRequired();
        for(int i = 0; i < list.size() && b; i++) {
            String s = list.get(i);
            CustomEnchantment custom = EnchantRegistry.getById(s.trim().toUpperCase());
            if(custom == null) continue;
            Enchantment enchantment = custom.getBukkitEnchantment();
            ItemStack iS = e.getPlayer().getInventory().getItemInMainHand();
            if(iS != null && iS.getType() != Material.AIR && enchantment != null) {
                if(!iS.containsEnchantment(enchantment)) b = false;
            }
        }
        if(!b) {
            e.setCancelled(true);
            LocaleAddon.sendMessage(e.getPlayer(), LocaleAddon.ENCHANTS_REQUIRED, new HashMap<>() {{
                put("%list%", String.join(", ", list));
            }});
        }
    }
}
