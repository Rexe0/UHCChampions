package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ModularBow implements Listener {
    private Map<UUID, Integer> modularMode = new HashMap<>();

    private String[] modeName = new String[]{
            "Punch",
            "Poison",
            "Lightning"
    };

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        modularMode.remove(e.getPlayer().getUniqueId());
    }
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if (!UHCChampions.isItem(e.getItem(), ChatColor.GREEN+"Modular Bow")) return;
        UUID uuid = e.getPlayer().getUniqueId();
        modularMode.putIfAbsent(uuid, 0);
        int mode = modularMode.get(uuid);
        mode = mode+1 > 2 ? 0 : mode+1;
        modularMode.put(uuid, mode);

        if (mode == 0) {
            e.getItem().addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
        } else
            e.getItem().removeEnchantment(Enchantment.ARROW_KNOCKBACK);
        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.CLICK, 1, 0);
        e.getPlayer().sendMessage(ChatColor.GREEN+"Changed mode to "+modeName[mode]);
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if (!UHCChampions.isItem(e.getBow(), ChatColor.GREEN+"Modular Bow")) return;
        if (!(e.getProjectile() instanceof Arrow)) return;
        Arrow arrow = (Arrow) e.getProjectile();
        modularMode.putIfAbsent(player.getUniqueId(), 0);
        if (modularMode.get(player.getUniqueId()) == 0) return;
        if (e.getForce() < 1) {
            player.sendMessage(ChatColor.RED+"You must fully charge your bow for the Modular Bow effects to work.");
            return;
        }
        arrow.setMetadata("modular" + modeName[modularMode.get(player.getUniqueId())], new FixedMetadataValue(UHCChampions.getInstance(), true));
    }
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Arrow)) return;
        if (!(e.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) e.getEntity();
        Arrow arrow = (Arrow) e.getDamager();
        if (arrow.hasMetadata("modularPoison")) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 50, 1));
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 50, 0));
        }
        if (arrow.hasMetadata("modularLightning")) {
            entity.getWorld().strikeLightningEffect(entity.getLocation());
            UHCChampions.dealTrueDamage(entity, 2);
        }
    }
}
