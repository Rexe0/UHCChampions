package me.rexe0.uhcchampions;

import org.bukkit.plugin.java.JavaPlugin;

public final class UHCChampions extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new PlayerHead(), this);
        getServer().addRecipe(PlayerHead.goldenHeadCraft());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
