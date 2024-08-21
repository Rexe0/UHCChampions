package me.rexe0.uhcchampions.config;

import me.rexe0.uhcchampions.UHCChampions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigLoader implements CommandExecutor {
    private FileConfiguration config;
    private boolean starPrefix;
    private boolean projectileIndicator;
    private boolean compassTracker;
    private boolean mobChanges;
    private boolean playerHeads;
    private boolean goldenHeads;
    private boolean doubleGoldenAppleHealing;
    private boolean spectatorFix;
    private Map<String, Map<String, Object>> itemMap;
    private List<String> disabledEnchantItems;

    public ConfigLoader(FileConfiguration config) {
        this.config = config;
        init();
    }

    private void init() {
        starPrefix = config.getBoolean("enable-star-prefix");
        projectileIndicator = config.getBoolean("enable-projectile-indicator");
        compassTracker = config.getBoolean("enable-compass-tracker");
        mobChanges = config.getBoolean("enable-mob-changes");
        playerHeads = config.getBoolean("enable-player-heads");
        goldenHeads = config.getBoolean("enable-golden-heads");
        doubleGoldenAppleHealing = config.getBoolean("enable-double-golden-apple-healing");
        doubleGoldenAppleHealing = config.getBoolean("enable-double-golden-apple-healing");
        spectatorFix = config.getBoolean("enable-spectator-fix");

        itemMap = new HashMap<>();
        for (String section : config.getConfigurationSection("items").getKeys(false)) {
            Map<String, Object> attributes = new HashMap<>();

            for (String str : config.getConfigurationSection("items."+section).getKeys(false)) {
                attributes.put(str, config.get("items."+section+"."+str));
            }

            itemMap.put(section, attributes);
        }

        disabledEnchantItems = new ArrayList<>(config.getStringList("item-enchant-disabled"));
    }

    public boolean isEnchantable(String name) {
        for (String itemName : disabledEnchantItems)
            if (name.equals(ChatColor.translateAlternateColorCodes('&', itemName))) return false;
        return true;
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
    public boolean getItemBoolean(String id, String attributeName) {
        return (boolean) getItemAttribute(id, attributeName);
    }
    public String getItemName(String id) {
        return ChatColor.translateAlternateColorCodes('&', "&a"+getItemAttribute(id, "name"));
    }
    public boolean isStarPrefix() {
        return starPrefix;
    }

    public boolean isProjectileIndicator() {
        return projectileIndicator;
    }

    public boolean isCompassTracker() {
        return compassTracker;
    }

    public boolean isMobChanges() {
        return mobChanges;
    }

    public boolean isPlayerHeads() {
        return playerHeads;
    }

    public boolean isGoldenHeads() {
        return goldenHeads;
    }

    public boolean isDoubleGoldenAppleHealing() {
        return doubleGoldenAppleHealing;
    }

    public boolean isSpectatorFix() {
        return spectatorFix;
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
