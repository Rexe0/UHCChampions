package me.rexe0.uhcchampions.scenarios;

import com.gmail.val59000mc.UhcCore;
import com.gmail.val59000mc.configuration.MainConfig;
import com.gmail.val59000mc.events.UhcStartedEvent;
import com.gmail.val59000mc.exceptions.UhcPlayerNotOnlineException;
import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.players.PlayerState;
import com.gmail.val59000mc.players.UhcPlayer;
import com.gmail.val59000mc.scenarios.ScenarioListener;
import me.rexe0.uhcchampions.UHCChampions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class EarlySurfaceListener extends ScenarioListener {
    // Ticks between damage
    private static final double damage = 4;

    public static List<String> getDescription() {
        List<String> desc = new ArrayList<>();
        desc.add("In this scenario, 2 minutes after grace period ends every player under y = 55 will start taking large damage.");
        return desc;
    }

    private final int period = 5*20;
    private long timeBeforePvp;
    private int taskId = -1;

    @EventHandler
    public void onGameStarted(UhcStartedEvent e){
        timeBeforePvp = e.getGameManager().getConfig().get(MainConfig.TIME_BEFORE_PVP);

        // 2 Mins after grace period ends
        taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(UHCChampions.getInstance(), new EarlySurfaceTask(this, true), (timeBeforePvp+120)*20);
    }
    @Override
    public void onDisable() {
        // stop task
        if (taskId != -1)
            Bukkit.getScheduler().cancelTask(taskId);
    }

    private static class EarlySurfaceTask implements Runnable {
        private final EarlySurfaceListener listener;
        private boolean shouldAnnounce;

        public EarlySurfaceTask(EarlySurfaceListener listener, boolean shouldAnnounce) {
            this.listener = listener;
            this.shouldAnnounce = shouldAnnounce;
        }

        public void run() {

            for (UhcPlayer uhcPlayer : GameManager.getGameManager().getPlayerManager().getOnlinePlayingPlayers()) {
                if (uhcPlayer.getState() == PlayerState.PLAYING) {
                    try {
                        Player player = uhcPlayer.getPlayer();
                        if (shouldAnnounce)
                            player.sendMessage( ChatColor.DARK_RED+"[Early Surface]"+ChatColor.RED+" Early Surface has started! Get to the surface or die!");

                        if (player.getLocation().getBlockY() < 55) {
                            player.sendMessage( ChatColor.DARK_RED+"[Early Surface]"+ChatColor.RED+" You are taking damage because you are not at the surface!");
                            UHCChampions.dealTrueDamage(player, damage);
                        }
                    } catch (UhcPlayerNotOnlineException ignored) {
                    }
                }
            }

            shouldAnnounce = false;
            this.listener.taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(UhcCore.getPlugin(), this, this.listener.period);
        }
    }
}
