package me.rexe0.uhcchampions;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Anduril {
    public static void andurilCheck(Player player) {
        ItemStack item = player.getItemInHand();
        if (!UHCChampions.isItem(item, ChatColor.GREEN+"Anduril")) return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 0));
    }
}
