package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.UUID;

public class AxeOfPerun implements Listener {
    private static final HashSet<UUID> perunCooldown = new HashSet<>();

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;
        Player damager = (Player) e.getDamager();
        if (perunCooldown.contains(damager.getUniqueId())) return;

        ItemStack item = VersionUtils.getVersionUtils().getItemInMainhand(damager);
        if (!UHCChampions.isItem(item, ChatColor.GREEN+"Axe of Perun")) return;

        LivingEntity entity = (LivingEntity) e.getEntity();

        perunCooldown.add(damager.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                perunCooldown.remove(damager.getUniqueId());
            }
        }.runTaskLater(UHCChampions.getInstance(), 100);

        entity.getWorld().strikeLightningEffect(entity.getEyeLocation());
        UHCChampions.dealTrueDamage(entity, 4);
    }
}
