package me.rexe0.uhcchampions;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Exodus implements Listener {
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
        if (!UHCChampions.isItem(helm, ChatColor.GREEN+"Exodus")) return;
        damager.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 51, 1));
    }
}
