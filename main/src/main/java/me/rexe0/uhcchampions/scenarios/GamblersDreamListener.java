package me.rexe0.uhcchampions.scenarios;

import com.gmail.val59000mc.customitems.Craft;
import com.gmail.val59000mc.customitems.CraftsManager;
import com.gmail.val59000mc.events.UhcStartedEvent;
import com.gmail.val59000mc.exceptions.UhcPlayerNotOnlineException;
import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.players.PlayerState;
import com.gmail.val59000mc.players.UhcPlayer;
import com.gmail.val59000mc.scenarios.ScenarioListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GamblersDreamListener extends ScenarioListener {

    public static List<String> getDescription() {
        List<String> desc = new ArrayList<>();
        desc.add("In this scenario, all players get the materials required to craft Dice of God at the start of the game.");
        return desc;
    }

    @EventHandler
    public void onGameStarted(UhcStartedEvent e){
        List<ItemStack> items = getDiceIngredients();
        for (UhcPlayer uhcPlayer : GameManager.getGameManager().getPlayerManager().getOnlinePlayingPlayers()) {
            if (uhcPlayer.getState() == PlayerState.PLAYING) {
                try {
                    Player player = uhcPlayer.getPlayer();
                    items.forEach(i -> player.getInventory().addItem(i));
                } catch (UhcPlayerNotOnlineException ignored) {
                }
            }
        }
    }

    private List<ItemStack> getDiceIngredients() {

        for (Craft craft : CraftsManager.getCrafts()) {
            ItemStack item = craft.getCraft();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) continue;
            if (!ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals("Dice of God")) continue;
            return new ArrayList<>(craft.getRecipe());
        }
        return new ArrayList<>();
    }
}
