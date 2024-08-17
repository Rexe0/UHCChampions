package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import me.rexe0.uhcchampions.util.PotionEffectType;

public class BarbarianChestplate {
    private static final String id = "barbarian-chestplate";
    public static void barbarianCheck(Player player) {
        ItemStack item = player.getEquipment().getChestplate();
        if (!UHCChampions.isItem(item, UHCChampions.getConfigLoader().getItemName(id))) return;
        player.addPotionEffect(new PotionEffect(VersionUtils.getVersionUtils().getPotionEffectType(PotionEffectType.STRENGTH), 30, 0));
        player.addPotionEffect(new PotionEffect(VersionUtils.getVersionUtils().getPotionEffectType(PotionEffectType.RESISTANCE), 30, 0));
    }
}
