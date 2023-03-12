package me.rexe0.uhcchampions.score;

import me.rexe0.uhcchampions.UHCChampions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class ScoreDisplay implements Listener {
    // Maps star level to score requirement
    private Map<Integer, Integer> starMap;

    public ScoreDisplay() {
        starMap = new HashMap<>();
        starMap.put(1, 0);
        for (int i = 2; i <= 15; i++) {
            starMap.put(i, (int) (5*Math.pow(2, i-2)));
        }
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (!UHCChampions.getInstance().getConfigLoader().isStarPrefix()) return;
        e.setFormat(ChatColor.GOLD+"["+getStarLevel(e.getPlayer())+"✫] "+ChatColor.RESET+e.getFormat());
    }
    public int getStarLevel(Player player) {
        int starLevel = 0;
        for (Map.Entry<Integer, Integer> entry : starMap.entrySet()) {
            if (ScoreFile.getPlayerData(player).getScore() >= entry.getValue()) starLevel = entry.getKey();
        }
        return starLevel;
    }
}
