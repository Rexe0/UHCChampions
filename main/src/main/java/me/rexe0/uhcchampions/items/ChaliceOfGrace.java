package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.config.ConfigLoader;
import me.rexe0.uhcchampions.util.Sound;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.Material;
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
    private static final String id = "chalice-of-grace";
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack item = e.getItem();
        ConfigLoader loader = UHCChampions.getConfigLoader();

        if (!UHCChampions.isItem(item, loader.getItemName(id))) return;
        Player player = e.getPlayer();

        chaliceEffect.add(player.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                chaliceEffect.remove(player.getUniqueId());
            }
        }.runTaskLater(UHCChampions.getInstance(), loader.getItemInteger(id, "duration"));

        player.playSound(player.getLocation(), Sound.IRON_GOLEM_DEATH.getSound(), 1, 0);
        int amount = item.getAmount();
        amount--;
        if (amount <= 0) {
            VersionUtils.getVersionUtils().setItemInMainhand(player, new ItemStack(Material.AIR));
        } else {
            item.setAmount(amount);
            VersionUtils.getVersionUtils().setItemInMainhand(player, item);
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

        double multiplier = UHCChampions.getConfigLoader().getItemDouble(id, "cap");
        new BukkitRunnable() {
            @Override
            public void run() {
                UHCChampions.dealTrueDamage(player, Math.min(VersionUtils.getVersionUtils().getMaxHealth(player)*multiplier, damage));
            }
        }.runTaskLater(UHCChampions.getInstance(), 1);
    }
}
