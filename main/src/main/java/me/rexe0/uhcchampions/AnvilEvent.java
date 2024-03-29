package me.rexe0.uhcchampions;

import me.rexe0.uhcchampions.util.Sound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilEvent implements Listener {
    @EventHandler
    public void onPrepareAnvil(InventoryClickEvent e) {
        if (e.isCancelled()) return;
        Player player = (Player) e.getWhoClicked();

        Inventory inv = e.getInventory();
        if (!(inv instanceof AnvilInventory)) return;
        if (!inv.equals(e.getView().getTopInventory())) return;
        ItemStack item = null;
        if (e.isShiftClick()) item = e.getCurrentItem();
        else if (e.isLeftClick()) {
            if (e.getRawSlot() != 0) return;
            item = e.getCursor();
        }
        if (item == null || item.getType() == Material.ENCHANTED_BOOK) return;

        if (anvilCheck(item)) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED+"You cannot enchant that item.");
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT.getSound(), 1, 0);
        }
    }
    @EventHandler
    public void onPrepareAnvil(InventoryDragEvent e) {
        if (e.isCancelled()) return;
        Player player = (Player) e.getWhoClicked();

        Inventory inv = e.getInventory();
        if (!(inv instanceof AnvilInventory)) return;
        if (!e.getRawSlots().contains(0)) return;
        ItemStack item = e.getOldCursor();

        if (item == null || item.getType() == Material.ENCHANTED_BOOK) return;

        if (anvilCheck(item)) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED+"You cannot enchant that item.");
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT.getSound(), 1, 0);
        }
    }

    @EventHandler
    public void onUseAnvil(InventoryClickEvent e) {
        if (e.isCancelled()) return;

        Inventory inv = e.getInventory();

        if (!(inv instanceof AnvilInventory)) return;
        if (!inv.equals(e.getView().getTopInventory())) return;
        if (e.getSlot() != 2) return;

        ItemStack item = inv.getItem(2);
        ItemStack ingredient = inv.getItem(0);

        if (item == null || item.getType() == Material.ENCHANTED_BOOK || ingredient == null || !ingredient.hasItemMeta() || !ingredient.getItemMeta().hasDisplayName()) return;

        String name = ingredient.getItemMeta().getDisplayName();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
    }


    private boolean anvilCheck(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return false;
        return !UHCChampions.getConfigLoader().isEnchantable(item.getItemMeta().getDisplayName());
    }
}
