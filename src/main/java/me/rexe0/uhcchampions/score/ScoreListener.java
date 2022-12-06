package me.rexe0.uhcchampions.score;

import com.gmail.val59000mc.events.UhcWinEvent;
import com.gmail.val59000mc.exceptions.UhcPlayerNotOnlineException;
import com.gmail.val59000mc.players.UhcPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ScoreListener implements Listener {
    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;
        ScoreFile.getPlayerData(killer).addKill();
    }

    @EventHandler
    public void onWin(UhcWinEvent e) throws UhcPlayerNotOnlineException {
        for (UhcPlayer p : e.getWinners()) {
            if (!p.isOnline()) continue;
            ScoreFile.getPlayerData(p.getPlayer()).addWin();
        }
    }
}
