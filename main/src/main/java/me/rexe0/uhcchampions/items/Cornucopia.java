package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.config.ConfigLoader;
import me.rexe0.uhcchampions.util.Sound;
import me.rexe0.uhcchampions.util.VersionUtils;
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
    private static final String id = "cornucopia";
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack item = e.getItem();
        ConfigLoader loader = UHCChampions.getConfigLoader();

        if (!UHCChampions.isItem(item, loader.getItemName(id))) return;
        Player player = e.getPlayer();

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, loader.getItemInteger(id, "regenDuration"), loader.getItemInteger(id, "regenAmp")));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, loader.getItemInteger(id, "saturationDuration"), loader.getItemInteger(id, "saturationAmp")));

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
