package me.rexe0.uhcchampions.items;

import com.gmail.val59000mc.game.GameManager;
import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.config.ConfigLoader;
import me.rexe0.uhcchampions.util.HomingArrowRunnable;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Random;

public class ArtemisBow implements Listener {
    private static final Random random = new Random();
    private static final String id = "artemis-bow";
    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        ConfigLoader loader = UHCChampions.getConfigLoader();

        if (!UHCChampions.isItem(e.getBow(), loader.getItemName(id))) return;
        if (!(e.getProjectile() instanceof Arrow)) return;

        GameManager manager = GameManager.getGameManager();
        // Ensure homing arrows don't fire before pvp starts
        if (!manager.getPvp()) return;

        if (random.nextInt(100) < loader.getItemInteger(id, "chance")) return;
        Arrow arrow = (Arrow) e.getProjectile();


        arrow.setMetadata("homingArrow", new FixedMetadataValue(UHCChampions.getInstance(), true));
        new HomingArrowRunnable(arrow, ((Player) e.getEntity()).getPlayer()).runTaskTimer(UHCChampions.getInstance(), 5, 1);
    }
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Arrow)) return;
        if (!(e.getEntity() instanceof LivingEntity)) return;
        Arrow arrow = (Arrow) e.getDamager();
        if (!arrow.hasMetadata("homingArrow")) return;

        ConfigLoader loader = UHCChampions.getConfigLoader();
        e.setDamage(e.getDamage()*loader.getItemDouble(id, "multiplier"));
    }
}
