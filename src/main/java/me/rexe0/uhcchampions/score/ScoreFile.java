package me.rexe0.uhcchampions.score;

import me.rexe0.uhcchampions.UHCChampions;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreFile {
    private static ScoreFile instance = new ScoreFile();

    public static ScoreFile getInstance() {
        return instance;
    }

    private final File file = new File(UHCChampions.getInstance().getDataFolder(), File.separator + "playerdata" + File.separator + "score.yml");
    private Map<UUID, Integer> playerScores = new HashMap<>();

    public void fileCheck() {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            try {
                configuration.createSection("Data");
                configuration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveData() {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        if (file.exists()) {
            try {
                for (Map.Entry<UUID, Integer> entry : playerScores.entrySet()) {
                    configuration.set("Data."+entry.getKey().toString(), entry.getValue());
                }
                configuration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadData() {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        for (String str : configuration.getConfigurationSection("Data").getKeys(false)) {
            playerScores.put(UUID.fromString(str), configuration.getInt("Data." + str));
        }
    }

    public void addScore(Player player, int score) {
        UUID uuid = player.getUniqueId();
        playerScores.putIfAbsent(uuid, 0);
        playerScores.put(uuid, playerScores.get(uuid)+score);
    }
    public int getScore(Player player) {
        playerScores.putIfAbsent(player.getUniqueId(), 0);
        return playerScores.get(player.getUniqueId());
    }
}
