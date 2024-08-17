package me.rexe0.uhcchampions.items;

import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.players.PlayerManager;
import com.gmail.val59000mc.players.UhcPlayer;
import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.config.ConfigLoader;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import me.rexe0.uhcchampions.util.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class FlaskOfCleansing implements Listener {
    private static HashSet<UUID> cleansedPlayers = new HashSet<>();
    private static List<PotionEffectType> positiveEffects;
    private static final String id = "flask-of-cleansing";

    static {
        positiveEffects = new ArrayList<>();
        positiveEffects.add(PotionEffectType.SPEED);
        positiveEffects.add(PotionEffectType.HASTE);
        positiveEffects.add(PotionEffectType.STRENGTH);
        positiveEffects.add(PotionEffectType.INSTANT_HEALTH);
        positiveEffects.add(PotionEffectType.JUMP_BOOST);
        positiveEffects.add(PotionEffectType.REGENERATION);
        positiveEffects.add(PotionEffectType.RESISTANCE);
        positiveEffects.add(PotionEffectType.FIRE_RESISTANCE);
        positiveEffects.add(PotionEffectType.WATER_BREATHING);
        positiveEffects.add(PotionEffectType.INVISIBILITY);
        positiveEffects.add(PotionEffectType.NIGHT_VISION);
        positiveEffects.add(PotionEffectType.ABSORPTION);
        positiveEffects.add(PotionEffectType.HEALTH_BOOST);
        positiveEffects.add(PotionEffectType.SATURATION);
    }

    public static void flaskCheck() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!cleansedPlayers.contains(player.getUniqueId())) continue;
            for (PotionEffectType type : positiveEffects) {
                if (!player.hasPotionEffect(VersionUtils.getVersionUtils().getPotionEffectType(type))) continue;
                player.removePotionEffect(VersionUtils.getVersionUtils().getPotionEffectType(type));
            }
        }
    }

    @EventHandler
    public void onHit(PotionSplashEvent e) {
        if (e.getEntity().getShooter() == null || !(e.getEntity().getShooter() instanceof Player)) return;
        ItemStack item = e.getPotion().getItem();
        ConfigLoader loader = UHCChampions.getConfigLoader();

        if (!UHCChampions.isItem(item, loader.getItemName(id))) return;
        PlayerManager manager = GameManager.getGameManager().getPlayerManager();
        Player player = (Player) e.getEntity().getShooter();
        UhcPlayer uhcPlayer = manager.getUhcPlayer((Player) e.getEntity().getShooter());

        int duration = loader.getItemInteger(id, "duration");

        for (Entity entity : e.getAffectedEntities()) {
            if (!(entity instanceof Player)) continue;
            if (entity.equals(player)) continue;
            if (uhcPlayer.isInTeamWith(manager.getUhcPlayer((Player)entity))) continue;
            Player p = (Player) entity;
            if (cleansedPlayers.contains(p.getUniqueId())) continue;
            p.sendMessage(ChatColor.RED+"You have been cursed by "+player.getName()+"! Positive potion effects will no longer work for "+(Math.round(duration/20d*10)/10d)+" seconds.");

            if (loader.getItemBoolean(id, "enable-weakness-effect"))
                p.addPotionEffect(new PotionEffect(VersionUtils.getVersionUtils().getPotionEffectType(PotionEffectType.WEAKNESS), duration, 0));

            cleansedPlayers.add(p.getUniqueId());
            new BukkitRunnable() {
                @Override
                public void run() {
                    cleansedPlayers.remove(p.getUniqueId());
                }
            }.runTaskLater(UHCChampions.getInstance(), loader.getItemInteger(id, "duration"));
        }
    }
}
