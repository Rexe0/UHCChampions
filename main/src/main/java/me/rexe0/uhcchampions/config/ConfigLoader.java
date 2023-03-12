package me.rexe0.uhcchampions.config;

import me.rexe0.uhcchampions.UHCChampions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigLoader implements CommandExecutor {
    private FileConfiguration config;
    private boolean starPrefix;

    public ConfigLoader(FileConfiguration config) {
        this.config = config;
        init();
    }

    private void init() {
        starPrefix = config.getBoolean("enable-star-prefix");
    }

    public boolean isStarPrefix() {
        return starPrefix;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        File file = new File(UHCChampions.getInstance().getDataFolder(), File.separator+"config.yml");
        if (!file.exists()) {
            UHCChampions.getInstance().saveDefaultConfig();
            return true;
        }
        UHCChampions.getInstance().reloadConfig();
        config = YamlConfiguration.loadConfiguration(file);

        init();
        sender.sendMessage(ChatColor.GREEN+"Successfully reload the config.yml of UHCChampions.");
        return true;
    }
}
