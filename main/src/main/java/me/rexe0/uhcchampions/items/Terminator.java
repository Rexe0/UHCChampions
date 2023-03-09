package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.util.Sound;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Terminator implements Listener {
    private Map<UUID, Integer> arrowsShot = new HashMap<>();
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        arrowsShot.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onClick(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        ItemStack item = e.getBow();
        if (!UHCChampions.isItem(item, ChatColor.GREEN+"Terminator")) return;
        Player player = (Player) e.getEntity();

        arrowsShot.putIfAbsent(player.getUniqueId(), 0);
        arrowsShot.put(player.getUniqueId(), arrowsShot.get(player.getUniqueId())+1);
        if (arrowsShot.get(player.getUniqueId()) < 5) return;
        arrowsShot.put(player.getUniqueId(), 0);
        e.getProjectile().setMetadata("terminatorArrow", new FixedMetadataValue(UHCChampions.getInstance(), true));
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Arrow)) return;
        Arrow damager = (Arrow) e.getDamager();

        if (!(damager.getShooter() instanceof Player) || !(e.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) e.getEntity();

        if (!damager.hasMetadata("terminatorArrow")) return;

        int arrowsInside = Math.min(4, VersionUtils.getVersionUtils().getArrowsStuckInBody(entity));
        e.setDamage(e.getDamage()+(arrowsInside*5));

        VersionUtils.getVersionUtils().setArrowsStuckInBody(entity, 0);

        Location location = entity.getLocation();
        entity.getWorld().playSound(location, Sound.WOODEN_DOOR_BREAK.getSound(), 1, 1);

        VersionUtils.getVersionUtils().spawnBloodParticle(location);
    }
}
