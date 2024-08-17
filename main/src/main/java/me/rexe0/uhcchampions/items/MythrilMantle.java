package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.config.ConfigLoader;
import me.rexe0.uhcchampions.util.PotionEffectType;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class MythrilMantle implements Listener {
    private static final String id = "mythril-mantle";

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Arrow)) return;
        Player player = (Player) e.getEntity();

        ItemStack item = player.getInventory().getChestplate();
        ConfigLoader loader = UHCChampions.getConfigLoader();

        if (!UHCChampions.isItem(item, loader.getItemName(id))) return;

        int duration = loader.getItemInteger(id, "duration");
        int amplifier = loader.getItemInteger(id, "amplifier");
        player.addPotionEffect(new PotionEffect(VersionUtils.getVersionUtils().getPotionEffectType(PotionEffectType.SPEED), duration, amplifier));
    }
}
