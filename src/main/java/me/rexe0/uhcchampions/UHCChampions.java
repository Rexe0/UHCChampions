package me.rexe0.uhcchampions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class UHCChampions extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new PlayerHead(), this);
        getServer().getPluginManager().registerEvents(new Exodus(), this);
        getServer().addRecipe(PlayerHead.goldenHeadCraft());

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers())
                    Anduril.andurilCheck(player);
            }
        }.runTaskTimer(this, 20, 10);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
