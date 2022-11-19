package me.rexe0.uhcchampions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class LavaPrevention implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity entity : player.getNearbyEntities(5, 5, 5)) {
                    if (!(entity instanceof Item)) return;
                    entity.setMetadata("dontBurn", new FixedMetadataValue(UHCChampions.getInstance(), true));
                }
            }
        }.runTaskLater(UHCChampions.getInstance(), 1);
    }
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Item)) return;
        if (e.getCause() != EntityDamageEvent.DamageCause.LAVA
                && e.getCause() != EntityDamageEvent.DamageCause.FIRE
                && e.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK) return;
        if (e.getEntity().getTicksLived() < 400) {
            e.setCancelled(true);
            return;
        }
        if (!e.getEntity().hasMetadata("dontBurn")) return;
        e.setCancelled(true);
    }
}
