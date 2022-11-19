package me.rexe0.uhcchampions;

import com.gmail.val59000mc.exceptions.UhcPlayerNotOnlineException;
import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.players.UhcPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CompassTracker implements Listener {
    private static Map<UUID, UUID> compassTarget = new HashMap<>();
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        compassTarget.remove(e.getPlayer().getUniqueId());
    }
    @EventHandler
    public void onClick(PlayerInteractEvent e) throws UhcPlayerNotOnlineException {
        if (e.getItem() == null || e.getItem().getType() != Material.COMPASS) return;
        GameManager manager = GameManager.getGameManager();
        if (!manager.getPvp()) {
            e.getPlayer().sendMessage(ChatColor.RED+"Compasses only work after Grace Period is over.");
            return;
        }
        UhcPlayer player = manager.getPlayerManager().getUhcPlayer(e.getPlayer());

        UhcPlayer target = null;
        double distance = Double.MAX_VALUE;
        for (UhcPlayer p : manager.getPlayerManager().getPlayersList()) {
            if (p.isInTeamWith(player)) continue;
            if (!p.getPlayer().getWorld().equals(player.getPlayer().getWorld())) continue;
            if (p.getPlayer().getLocation().distanceSquared(player.getPlayer().getLocation()) > distance) continue;
            distance = p.getPlayer().getLocation().distanceSquared(player.getPlayer().getLocation());
            target = p;
        }
        if (target == null) {
            e.getPlayer().sendMessage(ChatColor.RED+"No target found.");
            return;
        }
        compassTarget.put(player.getUuid(), target.getUuid());

        e.getPlayer().sendMessage(ChatColor.GREEN+"Pointing to closest enemy player ("+Math.round(Math.sqrt(distance))+" blocks)");
    }

    public static void run() {
        for (Map.Entry<UUID, UUID> entry : compassTarget.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            Player p = Bukkit.getPlayer(entry.getValue());
            if (player == null || p == null) continue;
            player.setCompassTarget(p.getLocation());
        }
    }
}
