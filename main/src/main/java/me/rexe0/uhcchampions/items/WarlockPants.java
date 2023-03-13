package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.config.ConfigLoader;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class WarlockPants implements Listener {
    private static final String id = "warlock-pants";

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();

        ItemStack item = player.getInventory().getLeggings();
        ConfigLoader loader = UHCChampions.getConfigLoader();

        if (!UHCChampions.isItem(item, loader.getItemName(id))) return;

        double multiplier = 1;
        multiplier -= loader.getItemDouble(id, "max-reduction")*(1-(player.getHealth()/ VersionUtils.getVersionUtils().getMaxHealth(player)));
        e.setDamage(e.getDamage()*multiplier);
    }
}
