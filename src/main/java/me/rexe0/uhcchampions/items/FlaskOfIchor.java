package me.rexe0.uhcchampions.items;

import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.players.PlayerManager;
import com.gmail.val59000mc.players.UhcPlayer;
import me.rexe0.uhcchampions.UHCChampions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FlaskOfIchor implements Listener {
    @EventHandler
    public void onHit(PotionSplashEvent e) {
        if (e.getEntity().getShooter() == null || !(e.getEntity().getShooter() instanceof Player)) return;
        ItemStack item = e.getPotion().getItem();
        if (!UHCChampions.isItem(item, ChatColor.GREEN+"Flask of Ichor")) return;
        PlayerManager manager = GameManager.getGameManager().getPlayerManager();
        Player player = (Player) e.getEntity().getShooter();
        UhcPlayer uhcPlayer = manager.getUhcPlayer((Player) e.getEntity().getShooter());

        for (Entity entity : e.getAffectedEntities()) {
            if (!(entity instanceof Player)) continue;
//            if (entity.equals(player)) continue;
//            if (uhcPlayer.isInTeamWith(manager.getUhcPlayer((Player)entity))) continue;
            Player p = (Player) entity;

            p.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 2));
        }
    }
}
