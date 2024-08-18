package me.rexe0.uhcchampions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class LavaPrevention implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if ((player.getLastDamageCause() != null && player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LAVA)
                || player.getLocation().getBlock().getType().toString().contains("LAVA")
                || player.getEyeLocation().getBlock().getType().toString().contains("LAVA")) {

            Location loc = player.getLocation();
            while (loc.getBlock().getType().toString().contains("LAVA")) {
                loc.add(0, 1, 0);
                if (loc.getBlockY() > 255) break;
            }
            spawnLootChest(loc, new ArrayList<>(e.getDrops()));
            e.getDrops().clear();
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity entity : player.getNearbyEntities(5, 5, 5)) {
                    if (!(entity instanceof Item)) continue;
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

    private void spawnLootChest(Location location, List<ItemStack> items) {
        Block block = location.getBlock();
        location.add(-1, 0, 0);
        Block block1 = location.getBlock();

        block.setType(Material.CHEST);
        block1.setType(Material.CHEST);

        Chest chest = (Chest) block.getState();
        Chest chest1 = (Chest) block.getState();

        for (int i = 0; i < items.size(); i++) {
            ItemStack drop = items.get(i);
            if (i < chest.getInventory().getSize())
                chest.getInventory().addItem(drop);
            else
                chest1.getInventory().addItem(drop);
        }

    }
}
