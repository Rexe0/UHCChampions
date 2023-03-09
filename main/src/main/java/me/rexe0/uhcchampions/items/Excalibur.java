package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.util.Sound;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Excalibur implements Listener {
    private Map<UUID, Integer> consecutiveHits = new HashMap<>();
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;
        Player damager = (Player) e.getDamager();

        ItemStack item = VersionUtils.getVersionUtils().getItemInMainhand(damager);
        if (!UHCChampions.isItem(item, ChatColor.GREEN+"Excalibur")) return;
        UUID uuid = damager.getUniqueId();
        consecutiveHits.putIfAbsent(uuid, 0);
        consecutiveHits.put(uuid, consecutiveHits.get(uuid)+1);

        if (consecutiveHits.get(uuid) < 3) return;
        LivingEntity entity = (LivingEntity) e.getEntity();

        consecutiveHits.put(uuid, 0);

        new BukkitRunnable() {
            @Override
            public void run() {
                UHCChampions.dealTrueDamage(entity, 4);
                entity.getWorld().playSound(entity.getEyeLocation(), Sound.EXPLODE.getSound(), 0.6f, 1);
            }
        }.runTaskLater(UHCChampions.getInstance(), 1);
    }
    @EventHandler
    public void onHitByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();

        ItemStack item = VersionUtils.getVersionUtils().getItemInMainhand(player);
        if (!UHCChampions.isItem(item, ChatColor.GREEN+"Excalibur")) return;
        UUID uuid = player.getUniqueId();
        consecutiveHits.put(uuid, 0);
    }
}
