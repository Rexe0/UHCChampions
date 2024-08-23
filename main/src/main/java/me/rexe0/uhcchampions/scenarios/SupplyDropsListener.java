package me.rexe0.uhcchampions.scenarios;

import com.gmail.val59000mc.UhcCore;
import com.gmail.val59000mc.configuration.MainConfig;
import com.gmail.val59000mc.events.UhcStartedEvent;
import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.maploader.MapLoader;
import com.gmail.val59000mc.scenarios.ScenarioListener;
import com.gmail.val59000mc.utils.LocationUtils;
import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.items.DiceOfGod;
import me.rexe0.uhcchampions.util.PotionEffectType;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;

public class SupplyDropsListener extends ScenarioListener {


    public static List<String> getDescription() {
        List<String> desc = new ArrayList<>();
        desc.add("In this scenario, once grace period ends, supply drops begin to fall from the sky at random locations");
        desc.add("The coordinates of these supply drops are broadcasted to all players in chat");
        desc.add("Supply drops contain useful materials and 1 custom item");
        return desc;
    }

    private final int period = 300*20;
    private long timeBeforePvp;
    private final MapLoader mapLoader = GameManager.getGameManager().getMapLoader();
    private int taskId = -1;

    @EventHandler
    public void onGameStarted(UhcStartedEvent e){
        timeBeforePvp = e.getGameManager().getConfig().get(MainConfig.TIME_BEFORE_PVP);

        // 2 Mins after grace period ends
        taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(UHCChampions.getInstance(), new SupplyDropTask(this), (timeBeforePvp+60)*20);
    }
    @Override
    public void onDisable() {
        // stop task
        if (taskId != -1)
            Bukkit.getScheduler().cancelTask(taskId);
    }

    private static class SupplyDropTask implements Runnable {
        private final SupplyDropsListener listener;

        public SupplyDropTask(SupplyDropsListener listener) {
            this.listener = listener;
        }

        public void run() {
            Location loc = LocationUtils.getRandomSpawnLocation(listener.mapLoader.getUhcWorld(World.Environment.NORMAL), (int) listener.mapLoader.getOverworldBorderApothem()/2);
            loc.setY(131);
            for (Player player : Bukkit.getOnlinePlayers())
                player.sendMessage(ChatColor.DARK_RED+"[Supply Drops]"+ChatColor.GOLD+" A supply drop is falling at "+ChatColor.WHITE+"X: "+loc.getBlockX()+" Z:"+loc.getBlockZ());

            SupplyDrop drop = new SupplyDrop(loc);
            drop.spawn();

            this.listener.taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(UhcCore.getPlugin(), this, this.listener.period);
        }
    }

    private static class SupplyDrop {
        private static final Vector[] parachuteLocation = new Vector[]{
                new Vector(1.5, 5, 1.5),
                new Vector(1.5, 5, -1.5),
                new Vector(-1.5, 5, 0)
        };
        private static final Random random = new Random();

        private Location location;

        public SupplyDrop(Location location) {
            this.location = location;
        }

        public void spawn() {
            new BukkitRunnable() {
                @Override
                public void run() {
                    OptionalDouble distance = Bukkit.getOnlinePlayers().stream()
                            .filter(p -> !GameManager.getGameManager().getPlayerManager().getUhcPlayer(p).isDead())
                            .mapToDouble(p -> p.getLocation().distanceSquared(location))
                            .min();
                    if (distance.isPresent() && distance.getAsDouble() <= 10000) {
                        createSupplyDrop();
                        cancel();
                        return;
                    }

                }
            }.runTaskTimer(UHCChampions.getInstance(), 20, 50);
        }
        private void createSupplyDrop() {
            FallingBlock block = location.getWorld().spawnFallingBlock(location, me.rexe0.uhcchampions.util.Material.SPAWNER.getMaterial(), (byte) 0);
            block.setDropItem(false);

            Chicken chicken = (Chicken) location.getWorld().spawnEntity(location, EntityType.CHICKEN);
            chicken.addPotionEffect(new PotionEffect(VersionUtils.getVersionUtils().getPotionEffectType(PotionEffectType.INVISIBILITY), 2000000, 0, true, false));
            chicken.addPotionEffect(new PotionEffect(VersionUtils.getVersionUtils().getPotionEffectType(PotionEffectType.RESISTANCE), 2000000, 0, true, false));
            chicken.setPassenger(block);

            for (int i = 0; i < 3; i++) {
                location.add(parachuteLocation[i]);
                Chicken parachute = (Chicken) location.getWorld().spawnEntity(location, EntityType.CHICKEN);
                parachute.setLeashHolder(chicken);
                location.subtract(parachuteLocation[i]);
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    OptionalDouble distance = Bukkit.getOnlinePlayers().stream()
                            .filter(p -> !GameManager.getGameManager().getPlayerManager().getUhcPlayer(p).isDead())
                            .mapToDouble(p -> p.getLocation().distanceSquared(location))
                            .min();
                    if (distance.isPresent() && distance.getAsDouble() > 12100) {
                        Location loc = chicken.getLocation();
                        while (loc.getBlockY() > 0) {
                            loc.subtract(0, 1, 0);
                            if (!loc.getBlock().getType().isSolid()) continue;
                            loc.add(0, 1, 0);
                            spawnChest(loc);
                            cancel();
                            return;
                        }
                    }
                    if (chicken.isOnGround()) {
                        spawnChest(chicken.getLocation());


                        cancel();
                        return;
                    }
                }

                private void spawnChest(Location location) {
                    Block blk = location.getBlock();
                    blk.setType(Material.CHEST);

                    Chest chest = (Chest) blk.getState();
                    chest.getBlockInventory().setContents(generateItems());

                    block.remove();
                    chicken.remove();
                }
            }.runTaskTimer(UHCChampions.getInstance(), 5, 10);
        }

        private ItemStack[] generateItems() {
            List<ItemStack> items = new ArrayList<>();
            items.add(DiceOfGod.generateRandomItem());
            items.add(new ItemStack(Material.DIAMOND, random.nextInt(3)));
            items.add(new ItemStack(Material.GOLDEN_APPLE, random.nextInt(2)));
            items.add(new ItemStack(Material.GOLD_INGOT, random.nextInt(5)));
            items.add(new ItemStack(Material.GOLD_INGOT, random.nextInt(5)));
            items.add(new ItemStack(Material.IRON_INGOT, random.nextInt(9)));
            items.add(new ItemStack(Material.IRON_INGOT, random.nextInt(9)));
            items.add(new ItemStack(Material.ARROW, 32));
            items.add(new ItemStack(Material.ARROW, 32));

            ItemStack[] inv = new ItemStack[27];
            boolean[] chosen = new boolean[27];

            for (ItemStack item : items) {
                if (item.getAmount() == 0) continue;;
                int j;
                do j = random.nextInt(27);
                while (chosen[j]);

                chosen[j] = true;
                inv[j] = item;
            }
            return inv;
        }
    }
}
