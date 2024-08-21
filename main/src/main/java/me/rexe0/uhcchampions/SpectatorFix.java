package me.rexe0.uhcchampions;

import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.players.PlayerManager;
import com.gmail.val59000mc.players.UhcPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;

public class SpectatorFix {
    private static final double MAX_DISTANCE = 100;
    public static void run() {
        if (!UHCChampions.getConfigLoader().isSpectatorFix()) return;


        PlayerManager manager = GameManager.getGameManager().getPlayerManager();
        for (Player player : Bukkit.getOnlinePlayers()) {
            UhcPlayer uhcPlayer = manager.getUhcPlayer(player);
            if (!uhcPlayer.isDead()) continue;

            boolean teamDead = uhcPlayer.getTeam().getMembers().stream()
                    .allMatch(UhcPlayer::isDead);

            Player closest = player.getWorld().getPlayers().stream()
                    .filter(p -> !p.equals(player))
                    .filter(p -> teamDead || uhcPlayer.isInTeamWith(manager.getUhcPlayer(p)))
                    .filter(p -> !manager.getUhcPlayer(p).isDead())
                    .min(Comparator.comparingDouble(p -> p.getLocation().distanceSquared(player.getLocation())))
                    .orElse(null);
            if (closest == null) continue;
            if (closest.getLocation().distanceSquared(player.getLocation()) < MAX_DISTANCE*MAX_DISTANCE) continue;
            player.teleport(closest);
        }
    }
}
