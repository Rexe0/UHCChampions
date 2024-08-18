package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.config.ConfigLoader;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.UUID;

public class AxeOfPerun implements Listener {
    private final String id = "axe-of-perun";
    private static final HashSet<UUID> perunCooldown = new HashSet<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;
        if (e.isCancelled()) return;

        Player damager = (Player) e.getDamager();
        if (perunCooldown.contains(damager.getUniqueId())) return;

        ItemStack item = VersionUtils.getVersionUtils().getItemInMainhand(damager);
        ConfigLoader loader = UHCChampions.getConfigLoader();

        if (!UHCChampions.isItem(item, loader.getItemName(id))) return;

        LivingEntity entity = (LivingEntity) e.getEntity();

        perunCooldown.add(damager.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                perunCooldown.remove(damager.getUniqueId());
            }
        }.runTaskLater(UHCChampions.getInstance(), loader.getItemInteger(id, "cooldown"));

        entity.getWorld().strikeLightningEffect(entity.getEyeLocation());
        UHCChampions.dealTrueDamage(entity, loader.getItemDouble(id, "damage"));
    }
}
