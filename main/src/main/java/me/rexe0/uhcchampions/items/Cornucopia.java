package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.util.Sound;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1200, 0));

        player.playSound(player.getLocation(), Sound.BURP.getSound(), 1, 2);
        int amount = item.getAmount();
        amount--;
        if (amount <= 0) {
            VersionUtils.getVersionUtils().setItemInMainhand(player, new ItemStack(Material.AIR));
        } else {
            item.setAmount(amount);
            VersionUtils.getVersionUtils().setItemInMainhand(player, item);
        }
        e.setCancelled(true);
    }
}
