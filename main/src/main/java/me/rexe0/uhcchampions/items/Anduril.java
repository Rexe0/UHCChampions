package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Anduril {
    public static void andurilCheck(Player player) {
        ItemStack item = VersionUtils.getVersionUtils().getItemInMainhand(player);
        if (!UHCChampions.isItem(item, ChatColor.GREEN+"Anduril")) return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30, 0));
    }
}
