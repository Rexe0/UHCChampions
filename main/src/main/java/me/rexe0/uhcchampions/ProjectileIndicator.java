package me.rexe0.uhcchampions;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ProjectileIndicator implements Listener {
    @EventHandler (priority = EventPriority.LOWEST)
    public void onArrowHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Arrow)) return;
        if (!(e.getEntity() instanceof Player) || !(((Arrow)e.getDamager()).getShooter() instanceof Player)) return;
        if (e.isCancelled()) return;
        if (!UHCChampions.getConfigLoader().isProjectileIndicator()) return;

        Player player = (Player) ((Arrow)e.getDamager()).getShooter();
        Player entity = (Player) e.getEntity();
        double damage = Math.floor(e.getFinalDamage()*100)/100f;
        player.sendMessage(ChatColor.AQUA+"Hit "+ChatColor.RED+entity.getName()+ChatColor.AQUA+" for "+damage+".");
    }

}
