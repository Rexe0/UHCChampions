package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.util.HomingArrowRunnable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Random;

public class ArtemisBow implements Listener {
    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!UHCChampions.isItem(e.getBow(), ChatColor.GREEN+"Artemis Bow")) return;
        if (!(e.getProjectile() instanceof Arrow)) return;
        Arrow arrow = (Arrow) e.getProjectile();
        if ((new Random()).nextInt(4) != 0) return;
        arrow.setMetadata("homingArrow", new FixedMetadataValue(UHCChampions.getInstance(), true));
        new HomingArrowRunnable(arrow, ((Player) e.getEntity()).getPlayer()).runTaskTimer(UHCChampions.getInstance(), 5, 1);
    }
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Arrow)) return;
        if (!(e.getEntity() instanceof LivingEntity)) return;
        Arrow arrow = (Arrow) e.getDamager();
        if (!arrow.hasMetadata("homingArrow")) return;
        e.setDamage(e.getDamage()*0.6f);
    }
}
