package me.rexe0.uhcchampions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Anduril {
    public static void andurilCheck(Player player) {
        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) return;
        if (!player.getItemInHand().getItemMeta().hasDisplayName()) return;
        if (!player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN+"Anduril")) return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 0));
    }
}
