package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.util.Sound;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class VoidGrimoire implements Listener {
    @EventHandler
    public void onPrepareAnvil(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getType() != InventoryType.ANVIL) return;
        final AnvilInventory inv = (AnvilInventory) e.getClickedInventory();
        // When player clicks result
        if (e.getSlot() == 2) {
            final Player player = (Player) e.getWhoClicked();
            if (!UHCChampions.isItem(inv.getItem(1), ChatColor.GREEN + "Void Grimoire") || inv.getItem(2) == null)
                return;
            new BukkitRunnable() {
                public void run() {
                    ItemStack item = inv.getItem(0).clone();
                    item = VersionUtils.getVersionUtils().applyEnchantmentTableLogic(item, 30);
                    player.setItemOnCursor(item);
                    inv.setItem(0, null);
                    inv.setItem(1, null);
                    inv.setItem(2, null);
                    player.playSound(player.getLocation(), Sound.ANVIL_USE.getSound(), 1, 1);
                }
            }.runTaskLater(UHCChampions.getInstance(), 1);
        }
        // Set Result item
        new BukkitRunnable() {
            public void run() {

                if ((e.getSlot() == 1 && inv.getItem(0) != null && UHCChampions.isItem(inv.getItem(e.getSlot()), ChatColor.GREEN + "Void Grimoire"))
                        || (e.getSlot() == 0 && inv.getItem(0) != null && UHCChampions.isItem(inv.getItem(1), ChatColor.GREEN + "Void Grimoire"))) {
                    String itemName = inv.getItem(0).getType().toString();
                    if (!(itemName.contains("HELMET") || itemName.contains("CHESTPLATE") || itemName.contains("LEGGINGS") || itemName.contains("BOOTS")
                            || itemName.contains("SWORD") || itemName.contains("BOW"))) return;
                    ItemStack item = inv.getItem(0).clone();
                    ItemMeta meta = item.getItemMeta();
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "LEVEL 30 ENCHANTMENT");
                    meta.setLore(lore);
                    item.setItemMeta(meta);

                    inv.setItem(2, item);
                }
            }
        }.runTaskLater(UHCChampions.getInstance(), 1);
    }
}
