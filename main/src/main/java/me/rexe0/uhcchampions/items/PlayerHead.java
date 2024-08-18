package me.rexe0.uhcchampions.items;

import com.gmail.val59000mc.exceptions.UhcPlayerNotOnlineException;
import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.players.PlayerManager;
import com.gmail.val59000mc.players.UhcPlayer;
import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.util.Sound;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.UUID;

public class PlayerHead implements Listener {
    private static final HashSet<UUID> headCooldown = new HashSet<>();
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent e) {
        if (!UHCChampions.getConfigLoader().isPlayerHeads()) return;
        Player player = e.getEntity();

        ItemStack item = VersionUtils.getVersionUtils().getPlayerSkull(player);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED+player.getName()+ChatColor.RESET+"'s Head");
        item.setItemMeta(meta);

        e.getDrops().add(item);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getItem() == null) return;
        if (e.getItem().getType() != me.rexe0.uhcchampions.util.Material.PLAYER_HEAD.getMaterial()) return;
        if (VersionUtils.getVersion().equals("1.8") && VersionUtils.getVersionUtils().getDurability(e.getItem()) != me.rexe0.uhcchampions.util.Material.PLAYER_HEAD.getData()) return;
        if (e.getItem().getItemMeta().hasDisplayName()) {
            if (!e.getItem().getItemMeta().getDisplayName().endsWith("Head")) return;
        }

        ItemStack item = e.getItem();
        Player player = e.getPlayer();

        if (headCooldown.contains(player.getUniqueId())) return;

        headCooldown.add(player.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                headCooldown.remove(player.getUniqueId());
            }
        }.runTaskLater(UHCChampions.getInstance(), 20);

        PlayerManager manager = GameManager.getGameManager().getPlayerManager();
        UhcPlayer uhcPlayer = manager.getUhcPlayer(player);

        if (!item.getItemMeta().hasDisplayName() || !item.getItemMeta().getDisplayName().equals(ChatColor.GOLD+"Golden Head")) {
            if (!UHCChampions.getConfigLoader().isPlayerHeads()) return;
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 1), true);
            player.sendMessage(ChatColor.GREEN+"You ate a "+ChatColor.RED+"Player Head"+ChatColor.GREEN+" which gave you Regeneration II for 5 seconds and Speed II for 20 seconds.");

                for (UhcPlayer p : manager.getPlayersList()) {
                    try {
                        if (p.equals(uhcPlayer)) continue;
                        if (!p.isInTeamWith(uhcPlayer)) continue;
                        p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                        p.sendMessage(ChatColor.GREEN + player.getName() + " ate a " + ChatColor.RED + "Player Head" + ChatColor.GREEN + " which gave you Regeneration II for 5 seconds.");

                    } catch (UhcPlayerNotOnlineException ex) {
                        // Ignore
                    }
                }
        } else {
            if (!UHCChampions.getConfigLoader().isGoldenHeads()) return;
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 520, 1), true);
            player.sendMessage(ChatColor.GREEN + "You ate a " + ChatColor.GOLD + "Golden Head" + ChatColor.GREEN + " which gave you Regeneration III for 5 seconds and Speed II for 26 seconds.");

            for (UhcPlayer p : manager.getPlayersList()) {
                try {
                    if (p.equals(uhcPlayer)) continue;
                    if (!p.isInTeamWith(uhcPlayer)) continue;
                    p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
                    p.sendMessage(ChatColor.GREEN + player.getName() + " ate a " + ChatColor.GOLD + "Golden Head" + ChatColor.GREEN + " which gave you Regeneration III for 5 seconds.");
                } catch (UhcPlayerNotOnlineException ex) {
                    // Ignore
                }
            }
        }
        player.playSound(player.getLocation(), Sound.BURP.getSound(), 1, 2);
        int amount = item.getAmount();
        amount--;
        if (amount <= 0) {
            VersionUtils.getVersionUtils().setItemInMainhand(player, new ItemStack(Material.AIR));
        } else {
            item.setAmount(amount);
            VersionUtils.getVersionUtils().setItemInMainhand(player, item);
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() != Material.GOLDEN_APPLE) return;
        if (!UHCChampions.getConfigLoader().isDoubleGoldenAppleHealing()) return;
        if (VersionUtils.getVersion().equals("1.8") && VersionUtils.getVersionUtils().getDurability(e.getItem()) != 0) return;
        Player player = e.getPlayer();

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2), true);
    }

    public static ShapedRecipe goldenHeadCraft() {
        ItemStack item = VersionUtils.getVersionUtils().getCustomSkull("http://textures.minecraft.net/texture/3bb612eb495ede2c5ca5178d2d1ecf1ca5a255d25dfc3c254bc47f6848791d8");
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD+"Golden Head");
        item.setItemMeta(meta);

        ShapedRecipe recipe = VersionUtils.getVersionUtils().createRecipe("golden_head", item);
        recipe.shape("%%%", "%$%", "%%%");
        recipe.setIngredient('%', Material.GOLD_INGOT);
        VersionUtils.getVersionUtils().setRecipeIngredient(recipe, '$', me.rexe0.uhcchampions.util.Material.PLAYER_HEAD.getMaterial(), me.rexe0.uhcchampions.util.Material.PLAYER_HEAD.getData());
        return recipe;
    }
}
