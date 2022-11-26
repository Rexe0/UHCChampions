package me.rexe0.uhcchampions.items;

import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.players.PlayerManager;
import com.gmail.val59000mc.players.UhcPlayer;
import me.rexe0.uhcchampions.UHCChampions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class FlaskOfCleansing implements Listener {
    private static HashSet<UUID> cleansedPlayers = new HashSet<>();
    private static List<PotionEffectType> positiveEffects;

    public FlaskOfCleansing() {
        positiveEffects = new ArrayList<>();
        positiveEffects.add(PotionEffectType.SPEED);
        positiveEffects.add(PotionEffectType.FAST_DIGGING);
        positiveEffects.add(PotionEffectType.INCREASE_DAMAGE);
        positiveEffects.add(PotionEffectType.HEAL);
        positiveEffects.add(PotionEffectType.JUMP);
        positiveEffects.add(PotionEffectType.REGENERATION);
        positiveEffects.add(PotionEffectType.DAMAGE_RESISTANCE);
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
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (!positiveEffects.contains(effect.getType())) continue;
                player.removePotionEffect(effect.getType());
            }
        }
    }

    @EventHandler
    public void onHit(PotionSplashEvent e) {
        if (e.getEntity().getShooter() == null || !(e.getEntity().getShooter() instanceof Player)) return;
        ItemStack item = e.getPotion().getItem();
        if (!UHCChampions.isItem(item, ChatColor.GREEN+"Flask of Cleansing")) return;
        PlayerManager manager = GameManager.getGameManager().getPlayerManager();
        Player player = (Player) e.getEntity().getShooter();
        UhcPlayer uhcPlayer = manager.getUhcPlayer((Player) e.getEntity().getShooter());

        for (Entity entity : e.getAffectedEntities()) {
            if (!(entity instanceof Player)) continue;
            if (entity.equals(player)) continue;
            if (uhcPlayer.isInTeamWith(manager.getUhcPlayer((Player)entity))) continue;
            Player p = (Player) entity;
            if (cleansedPlayers.contains(p.getUniqueId())) continue;
            p.sendMessage(ChatColor.RED+"You have been cursed by "+player.getName()+"! Positive potion effects will no longer work for 10 seconds.");

            p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 0));
            cleansedPlayers.add(p.getUniqueId());
            new BukkitRunnable() {
                @Override
                public void run() {
                    cleansedPlayers.remove(p.getUniqueId());
                }
            }.runTaskLater(UHCChampions.getInstance(), 200);
        }
    }
}
