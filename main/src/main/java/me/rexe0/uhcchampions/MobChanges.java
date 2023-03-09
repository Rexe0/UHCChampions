package me.rexe0.uhcchampions;

import me.rexe0.uhcchampions.util.VersionUtils;
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
                if (VersionUtils.getVersion().equals("1.8")) {
                    Skeleton skeleton = ((Skeleton)location.getWorld().spawnEntity(location, EntityType.SKELETON));
                    skeleton.setSkeletonType(Skeleton.SkeletonType.WITHER);
                    skeleton.getEquipment().setItemInHand(new ItemStack(Material.STONE_SWORD));
                } else {
                    WitherSkeleton skeleton = ((WitherSkeleton)location.getWorld().spawnEntity(location, EntityType.WITHER_SKELETON));
                    skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.STONE_SWORD));
                }
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
            Skeleton skeleton = (Skeleton) entity;
            if (skeleton.getSkeletonType() == Skeleton.SkeletonType.WITHER) {
                if (rand.nextInt(5) == 0) {
                    ItemStack item;
                    if (VersionUtils.getVersion().equals("1.8"))
                        item = new ItemStack(me.rexe0.uhcchampions.util.Material.WITHER_SKELETON_SKULL.getMaterial(), 1, me.rexe0.uhcchampions.util.Material.WITHER_SKELETON_SKULL.getData());
                    else item = new ItemStack(me.rexe0.uhcchampions.util.Material.WITHER_SKELETON_SKULL.getMaterial(), 1);
                    entity.getWorld().dropItemNaturally(entity.getLocation(), item);
                }
            } else {
                for (ItemStack item : e.getDrops())
                    if (item.getType() == Material.BOW && rand.nextInt(2) == 0) item.setDurability((short) 0);
            }
            return;
        }
        if (entity instanceof Ghast) {
            entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(me.rexe0.uhcchampions.util.Material.NETHER_WART.getMaterial(), 1));
            return;
        }
    }
}
