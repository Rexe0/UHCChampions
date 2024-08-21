package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.config.ConfigLoader;
import me.rexe0.uhcchampions.util.PotionEffectType;
import me.rexe0.uhcchampions.util.Sound;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class ChaliceOfGrace implements Listener {
    private static final String id = "chalice-of-grace";

    @EventHandler
    public void onPlace(PlayerInteractEvent e) {
        if (!e.getAction().toString().contains("RIGHT")) return;
        ConfigLoader loader = UHCChampions.getConfigLoader();
        if (!UHCChampions.isItem(e.getItem(), loader.getItemName(id))) return;
        e.setCancelled(true);
    }
    @EventHandler
    public void onHit(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if (e.isCancelled()) return;

        if (player.getHealth()-e.getFinalDamage() > 1) return;
        ConfigLoader loader = UHCChampions.getConfigLoader();

        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (!UHCChampions.isItem(item, loader.getItemName(id))) continue;

            if (trigger(player, item)) {
                item.setType(Material.AIR);
                contents[i] = item;
                player.getInventory().setContents(contents);
            }
            e.setCancelled(true);
            return;
        }
    }

    private boolean trigger(Player player, ItemStack item) {
        ConfigLoader loader = UHCChampions.getConfigLoader();
        player.setHealth(5);
        player.addPotionEffect(new PotionEffect(VersionUtils.getVersionUtils().getPotionEffectType(PotionEffectType.RESISTANCE), loader.getItemInteger(id, "duration"), 4), true);

        player.getWorld().playSound(player.getLocation(), Sound.IRON_GOLEM_DEATH.getSound(), 1, 0);
        int amount = item.getAmount();
        amount--;
        if (amount <= 0)
            return true;
        item.setAmount(amount - 1);
        return false;
    }
}
