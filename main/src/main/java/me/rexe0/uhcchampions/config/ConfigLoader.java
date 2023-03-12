package me.rexe0.uhcchampions.config;

import me.rexe0.uhcchampions.UHCChampions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigLoader implements CommandExecutor {
    private FileConfiguration config;
    private boolean starPrefix;
    private Map<String, Map<String, Object>> itemMap;

    public ConfigLoader(FileConfiguration config) {
        this.config = config;
        init();
    }

    private void init() {
        starPrefix = config.getBoolean("enable-star-prefix");

        itemMap = new HashMap<>();
        for (String section : config.getConfigurationSection("items").getKeys(false)) {
            Map<String, Object> attributes = new HashMap<>();

            for (String str : config.getConfigurationSection("items."+section).getKeys(false)) {
                attributes.put(str, config.get("items."+section+"."+str));
            }

            itemMap.put(section, attributes);
        }
    }

    public Object getItemAttribute(String id, String attributeName) {
        return itemMap.get(id).get(attributeName);
    }
    public int getItemInteger(String id, String attributeName) {
        return (int) getItemAttribute(id, attributeName);
    }
    public double getItemDouble(String id, String attributeName) {
        return (double) getItemAttribute(id, attributeName);
    }
    public String getItemString(String id, String attributeName) {
        return (String) getItemAttribute(id, attributeName);
    }
    public String getItemName(String id) {
        return ChatColor.translateAlternateColorCodes('&', (String) getItemAttribute(id, "name"));
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
