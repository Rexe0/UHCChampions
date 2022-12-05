package me.rexe0.uhcchampions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StrengthNerf implements Listener {
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        Player player = (Player) e.getDamager();
        if (!player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) return;
        int level = 0;
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType() == PotionEffectType.INCREASE_DAMAGE) {
                level = effect.getAmplifier();
                break;
            }
        }
        double damage = e.getDamage()/(1+(level*1.3));
        damage *= 1+(level*0.3);
        e.setDamage(damage);
    }
}
