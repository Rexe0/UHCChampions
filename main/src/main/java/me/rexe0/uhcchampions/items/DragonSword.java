package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.util.Attribute;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DragonSword implements Listener {
    @EventHandler
    public void onHit(CraftItemEvent e) {
        ItemStack result = e.getRecipe().getResult();
        if (!UHCChampions.isItem(result, ChatColor.GREEN+"Dragon Sword")) return;
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN+"Dragon Sword");
        item.setItemMeta(meta);
        e.getInventory().setResult(VersionUtils.getVersionUtils().addAttribute(item, Attribute.GENERIC_ATTACK_DAMAGE, 8, 0, "HAND"));
    }
}
