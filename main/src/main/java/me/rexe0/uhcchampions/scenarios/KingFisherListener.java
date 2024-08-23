package me.rexe0.uhcchampions.scenarios;

import com.gmail.val59000mc.scenarios.ScenarioListener;
import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.config.ConfigLoader;
import me.rexe0.uhcchampions.items.DiceOfGod;
import me.rexe0.uhcchampions.util.Sound;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KingFisherListener extends ScenarioListener {
    private static final Random random = new Random();
    private static final String id = "kings-rod";

    public static List<String> getDescription() {
        List<String> desc = new ArrayList<>();
        desc.add("In this scenario, there is a small chance to fish up a custom item when using the King's Rod.");
        return desc;
    }

    // Percent chance of fishing up custom item.
    private final int chance = 5;

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        if (random.nextInt(100) >= chance) return;

        ItemStack mainhand = VersionUtils.getVersionUtils().getItemInMainhand(e.getPlayer());
        ConfigLoader loader = UHCChampions.getConfigLoader();

        if (!UHCChampions.isItem(mainhand, loader.getItemName(id))) return;

        ItemStack itemStack = DiceOfGod.generateRandomItem();
        if (itemStack == null) return;

        Entity caught = e.getCaught();
        Player player = e.getPlayer();

        Item item = caught.getWorld().dropItem(caught.getLocation(), itemStack);
        Bukkit.getScheduler().runTaskLater(UHCChampions.getInstance(), () -> item.setVelocity(caught.getVelocity()), 1);
        caught.remove();

        player.playSound(player.getLocation(), Sound.LEVEL_UP.getSound(), 1, 1.5f);
    }
}
