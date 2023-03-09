package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.util.VersionUtils;
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

public class DeathScythe implements Listener {
    private static final HashSet<UUID> scytheCooldown = new HashSet<>();

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;
        Player damager = (Player) e.getDamager();
        if (scytheCooldown.contains(damager.getUniqueId())) return;

        ItemStack item = VersionUtils.getVersionUtils().getItemInMainhand(damager);
        if (!UHCChampions.isItem(item, ChatColor.GREEN+"Death's Scythe")) return;

        LivingEntity entity = (LivingEntity) e.getEntity();

        scytheCooldown.add(damager.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                scytheCooldown.remove(damager.getUniqueId());
            }
        }.runTaskLater(UHCChampions.getInstance(), 60);
        new BukkitRunnable() {
            @Override
            public void run() {
                UHCChampions.dealTrueDamage(entity, entity.getHealth()*0.2f);
            }
        }.runTaskLater(UHCChampions.getInstance(), 1);

        if (VersionUtils.getVersionUtils().getDurability(item) == 250) {
            VersionUtils.getVersionUtils().setItemInMainhand(damager, new ItemStack(Material.AIR));
            return;
        }
        VersionUtils.getVersionUtils().setDurability(item, VersionUtils.getVersionUtils().getDurability(item)+1);
    }
}
