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

    private static final File file = new File(UHCChampions.getInstance().getDataFolder(), File.separator + "playerdata" + File.separator + "score.yml");
    private static Map<UUID, PlayerData> playerData = new HashMap<>();

    public static void fileCheck() {
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

    public static void saveData() {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        if (file.exists()) {
            try {
                for (Map.Entry<UUID, PlayerData> entry : playerData.entrySet()) {
                    configuration.set("Data."+entry.getKey().toString()+".Kills", entry.getValue().getKills());
                    configuration.set("Data."+entry.getKey().toString()+".Wins", entry.getValue().getWins());
                }
                configuration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadData() {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        for (String str : configuration.getConfigurationSection("Data").getKeys(false)) {
            UUID uuid = UUID.fromString(str);
            int kills = configuration.getInt("Data."+str+".Kills");
            int wins = configuration.getInt("Data."+str+".Wins");

            playerData.put(uuid, new PlayerData(uuid, wins, kills));
        }
    }

    public static PlayerData getPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        playerData.putIfAbsent(uuid, new PlayerData(uuid,0, 0));
        return playerData.get(player.getUniqueId());
    }
}
