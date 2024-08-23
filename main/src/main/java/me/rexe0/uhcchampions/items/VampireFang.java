package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.config.ConfigLoader;
import me.rexe0.uhcchampions.util.PotionEffectType;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class VampireFang implements Listener {
    private static final String id = "vampire-fang";

    @EventHandler(priority = EventPriority.HIGH)
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;
        if (e.isCancelled()) return;
        Player damager = (Player) e.getDamager();

        ItemStack item = VersionUtils.getVersionUtils().getItemInMainhand(damager);
        ConfigLoader loader = UHCChampions.getConfigLoader();

        if (!UHCChampions.isItem(item, loader.getItemName(id))) return;

        LivingEntity entity = (LivingEntity) e.getEntity();

        org.bukkit.potion.PotionEffectType regen = VersionUtils.getVersionUtils().getPotionEffectType(PotionEffectType.REGENERATION);
        if (entity.hasPotionEffect(regen)) {
            PotionEffect effect = VersionUtils.getVersionUtils().getPotionEffect(entity, PotionEffectType.REGENERATION);
            entity.removePotionEffect(regen);
            if (!damager.hasPotionEffect(regen)
                    || VersionUtils.getVersionUtils().getPotionEffect(damager, PotionEffectType.REGENERATION).getAmplifier() <= effect.getAmplifier()) {
                damager.addPotionEffect(new PotionEffect(regen, effect.getDuration(), effect.getAmplifier()), true);
            }
        }

        org.bukkit.potion.PotionEffectType absorption = VersionUtils.getVersionUtils().getPotionEffectType(PotionEffectType.ABSORPTION);
        if (entity.hasPotionEffect(absorption)) {
            PotionEffect effect = VersionUtils.getVersionUtils().getPotionEffect(entity, PotionEffectType.ABSORPTION);
            entity.removePotionEffect(absorption);

            if (!damager.hasPotionEffect(absorption)
                    || VersionUtils.getVersionUtils().getPotionEffect(damager, PotionEffectType.ABSORPTION).getAmplifier() <= effect.getAmplifier()) {
                damager.addPotionEffect(new PotionEffect(absorption, effect.getDuration(), effect.getAmplifier()), true);
            }
        }
    }
}
