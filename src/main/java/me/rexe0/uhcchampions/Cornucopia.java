package me.rexe0.uhcchampions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Cornucopia implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack item = e.getItem();
        if (!UHCChampions.isItem(item, ChatColor.GREEN+"Cornucopia")) return;
        Player player = e.getPlayer();

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1200, 0));

        player.playSound(player.getLocation(), Sound.BURP, 1, 2);
        int amount = item.getAmount();
        amount--;
        if (amount <= 0) {
            player.getInventory().setItemInHand(new ItemStack(Material.AIR));
        } else {
            item.setAmount(amount);
            player.getInventory().setItemInHand(item);
        }
        e.setCancelled(true);
    }
}
