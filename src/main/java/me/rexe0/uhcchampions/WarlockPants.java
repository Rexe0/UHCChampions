package me.rexe0.uhcchampions;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class WarlockPants implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player entity = (Player) e.getEntity();

        ItemStack item = entity.getInventory().getLeggings();
        if (!UHCChampions.isItem(item, ChatColor.GREEN+"Warlock Pants")) return;

        double multiplier = 1;
        multiplier -= 0.2*(1-(entity.getHealth()/entity.getMaxHealth()));
        e.setDamage(e.getDamage()*multiplier);
    }
}
