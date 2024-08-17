package me.rexe0.uhcchampions;

import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import me.rexe0.uhcchampions.util.PotionEffectType;

public class StrengthNerf implements Listener {
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        // Only nerf strength in 1.8. Strength was nerfed in base game by Mojang in 1.9+
        if (!VersionUtils.getVersion().equals("1.8")) return;

        Player player = (Player) e.getDamager();
        if (!player.hasPotionEffect(VersionUtils.getVersionUtils().getPotionEffectType(PotionEffectType.STRENGTH))) return;
        int level = 0;
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().equals(VersionUtils.getVersionUtils().getPotionEffectType(PotionEffectType.STRENGTH))) {
                level = effect.getAmplifier()+1;
                break;
            }
        }
        double damage = e.getDamage()/(1+(level*1.3));
        damage *= 1+(level*0.3);
        e.setDamage(damage);
    }
}
