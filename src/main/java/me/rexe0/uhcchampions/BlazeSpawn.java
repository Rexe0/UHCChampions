package me.rexe0.uhcchampions;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Random;

public class BlazeSpawn implements Listener {
    private Random rand = new Random();
    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) return;
        if (!(e.getEntity() instanceof PigZombie)) return;
        if (rand.nextInt(3) != 0) return;
        Location location = e.getEntity().getLocation();
        location.getWorld().spawnEntity(location, EntityType.BLAZE);

        e.setCancelled(true);

    }
}
