package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BarbarianChestplate {
    private static final String id = "barbarian-chestplate";
    public static void barbarianCheck(Player player) {
        ItemStack item = player.getEquipment().getChestplate();
        if (!UHCChampions.isItem(item, UHCChampions.getConfigLoader().getItemName(id))) return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 30, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30, 0));
    }
}
