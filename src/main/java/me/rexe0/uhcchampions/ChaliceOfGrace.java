package me.rexe0.uhcchampions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.UUID;

public class ChaliceOfGrace implements Listener {
    private final HashSet<UUID> chaliceEffect = new HashSet<>();
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack item = e.getItem();
        if (!UHCChampions.isItem(item, ChatColor.GREEN+"Chalice of Grace")) return;
        Player player = e.getPlayer();

        chaliceEffect.add(player.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                chaliceEffect.remove(player.getUniqueId());
            }
        }.runTaskLater(UHCChampions.getInstance(), 100);

        player.playSound(player.getLocation(), Sound.IRONGOLEM_DEATH, 1, 0);
        int amount = item.getAmount();
        amount--;
        if (amount <= 0) {
            player.getInventory().setItemInHand(new ItemStack(Material.AIR));
        } else {
            item.setAmount(amount);
            player.getInventory().setItemInHand(item);
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof LivingEntity)) return;
        Player player = (Player) e.getEntity();
        if (!chaliceEffect.contains(player.getUniqueId()) || e.isCancelled()) return;

        double damage = e.getFinalDamage();
        e.setDamage(0.1);
        new BukkitRunnable() {
            @Override
            public void run() {
                UHCChampions.dealTrueDamage(player, Math.min(player.getMaxHealth()*0.05f, damage));
            }
        }.runTaskLater(UHCChampions.getInstance(), 1);
    }
}
