package me.rexe0.uhcchampions;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class UHCChampions extends JavaPlugin {
    private static UHCChampions instance;

    public static UHCChampions getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new PlayerHead(), this);
        getServer().getPluginManager().registerEvents(new Exodus(), this);
        getServer().getPluginManager().registerEvents(new DragonSword(), this);
        getServer().getPluginManager().registerEvents(new AxeOfPerun(), this);
        getServer().addRecipe(PlayerHead.goldenHeadCraft());

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers())
                    Anduril.andurilCheck(player);
            }
        }.runTaskTimer(this, 20, 10);
    }

    public static void dealTrueDamage(LivingEntity entity, double amount) {
        double health = entity.getHealth();
        health -= amount;
        if (health <= 0) {
            entity.setHealth(1);
            entity.damage(20);
        } else {
            entity.setHealth(health);
            entity.damage(0.001);
        }
    }
}
