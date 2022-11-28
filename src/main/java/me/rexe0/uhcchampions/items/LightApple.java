package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class LightApple implements Listener {
    @EventHandler
    public void onHit(CraftItemEvent e) {
        ItemStack result = e.getRecipe().getResult();
        if (!UHCChampions.isItem(result, ChatColor.GREEN+"Light Apple")) return;
        e.getInventory().setResult(new ItemStack(Material.GOLDEN_APPLE));
    }
}
