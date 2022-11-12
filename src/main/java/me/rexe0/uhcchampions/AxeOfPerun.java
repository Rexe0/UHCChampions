package me.rexe0.uhcchampions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
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

        ItemStack item = damager.getInventory().getItemInHand();
        if (item == null || item.getType() == Material.AIR) return;
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
        if (!item.getItemMeta().getDisplayName().equals(ChatColor.GREEN+"Axe of Perun")) return;

        LivingEntity entity = (LivingEntity) e.getEntity();

        perunCooldown.add(damager.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                perunCooldown.remove(damager.getUniqueId());
            }
        }.runTaskLater(UHCChampions.getInstance(), 160);

        entity.getWorld().strikeLightningEffect(entity.getEyeLocation());
        UHCChampions.dealTrueDamage(entity, 2);
    }
}
