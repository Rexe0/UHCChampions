package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.config.ConfigLoader;
import me.rexe0.uhcchampions.util.Sound;
import org.bukkit.ChatColor;
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
    private static final String id = "modular-bow";

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
        ConfigLoader loader = UHCChampions.getConfigLoader();

        if (!UHCChampions.isItem(e.getItem(), loader.getItemName(id))) return;
        UUID uuid = e.getPlayer().getUniqueId();
        modularMode.putIfAbsent(uuid, 0);
        int mode = modularMode.get(uuid);
        mode = mode+1 > 2 ? 0 : mode+1;
        modularMode.put(uuid, mode);

        if (mode == 0) {
            e.getItem().addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
        } else
            e.getItem().removeEnchantment(Enchantment.ARROW_KNOCKBACK);
        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.CLICK.getSound(), 1, 0);
        e.getPlayer().sendMessage(ChatColor.GREEN+"Changed mode to "+modeName[mode]);
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        ConfigLoader loader = UHCChampions.getConfigLoader();

        if (!UHCChampions.isItem(e.getBow(), loader.getItemName(id))) return;
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

        ConfigLoader loader = UHCChampions.getConfigLoader();

        if (arrow.hasMetadata("modularPoison")) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 50, 1));
            if (loader.getItemBoolean(id, "enable-poison-slow"))
                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 50, 0));
        }
        if (arrow.hasMetadata("modularLightning")) {
            entity.getWorld().strikeLightningEffect(entity.getLocation());
            UHCChampions.dealTrueDamage(entity, loader.getItemDouble(id, "damage"));
        }
    }
}
