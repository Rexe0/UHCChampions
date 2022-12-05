package me.rexe0.uhcchampions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class MobChanges implements Listener {
    private Random rand = new Random();
    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) return;
        if (!(e.getEntity() instanceof PigZombie)) return;
        if (rand.nextInt(3) == 0) {
            Location location = e.getEntity().getLocation();
            e.setCancelled(true);
            if (rand.nextInt(3) == 0) {
                ((Skeleton)location.getWorld().spawnEntity(location, EntityType.SKELETON)).setSkeletonType(Skeleton.SkeletonType.WITHER);
                return;
            }
            location.getWorld().spawnEntity(location, EntityType.BLAZE);
        }

    }
    @EventHandler
    public void onShear(PlayerShearEntityEvent e) {
        if (!(e.getEntity() instanceof Sheep)) return;
        if (rand.nextInt(3) != 0) return;
        e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), new ItemStack(Material.STRING));
    }
    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        if (entity instanceof Spider) {
            entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.STRING));
            return;
        }
        if (entity instanceof Chicken) {
            entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.FEATHER));
            return;
        }
        if (entity instanceof Skeleton) {
            for (ItemStack item : e.getDrops())
                if (item.getType() == Material.BOW && rand.nextInt(5) == 0) item.setDurability((short)0);
            return;
        }
        if (entity instanceof Ghast) {
            entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.NETHER_WARTS));
            return;
        }
    }
}
