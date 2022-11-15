package me.rexe0.uhcchampions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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

        if (anvilCheck(item)) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED+"You cannot enchant that item.");
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
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

        if (anvilCheck(item)) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED+"You cannot enchant that item.");
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0);
        }
    }


    private boolean anvilCheck(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return false;
        return  (item.getItemMeta().getDisplayName().startsWith(ChatColor.GREEN+"") && !UHCChampions.isItem(item, ChatColor.GREEN+"Dragon Sword"));
    }
}
