package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.config.ConfigLoader;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Exodus implements Listener {
    private static final String id = "exodus";
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        Player damager = null;
        if (!(e.getEntity() instanceof Player)) return;
        if (e.getDamager() instanceof Projectile) {
            Projectile proj = (Projectile) e.getDamager();
            if (proj.getShooter() != null && proj.getShooter() instanceof Player) {
                damager = (Player) proj.getShooter();
            } else return;
        } else {
            if (!(e.getDamager() instanceof Player)) return;
            damager = (Player) e.getDamager();
        }
        ItemStack helm = damager.getInventory().getHelmet();
        ConfigLoader loader = UHCChampions.getConfigLoader();

        if (!UHCChampions.isItem(helm, loader.getItemName(id))) return;
        damager.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION
                , loader.getItemInteger(id, "regenDuration")
                , loader.getItemInteger(id, "regenAmp")));
    }
}
