package me.rexe0.uhcchampions.items;

import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.players.PlayerManager;
import com.gmail.val59000mc.players.UhcPlayer;
import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.config.ConfigLoader;
import me.rexe0.uhcchampions.util.PotionEffectType;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class FlaskOfIchor implements Listener {
    private static final String id = "flask-of-ichor";
    @EventHandler
    public void onHit(PotionSplashEvent e) {
        if (e.getEntity().getShooter() == null || !(e.getEntity().getShooter() instanceof Player)) return;
        ItemStack item = e.getPotion().getItem();
        ConfigLoader loader = UHCChampions.getConfigLoader();

        if (!UHCChampions.isItem(item, loader.getItemName(id))) return;
        PlayerManager manager = GameManager.getGameManager().getPlayerManager();
        Player player = (Player) e.getEntity().getShooter();
        UhcPlayer uhcPlayer = manager.getUhcPlayer((Player) e.getEntity().getShooter());

        for (Entity entity : e.getAffectedEntities()) {
            if (!(entity instanceof Player)) continue;
            if (entity.equals(player)) continue;
            if (uhcPlayer.isInTeamWith(manager.getUhcPlayer((Player)entity))) continue;
            Player p = (Player) entity;

            p.addPotionEffect(new PotionEffect(VersionUtils.getVersionUtils().getPotionEffectType(PotionEffectType.INSTANT_DAMAGE), 1, loader.getItemInteger(id, "amplifier")));
        }
    }
}
