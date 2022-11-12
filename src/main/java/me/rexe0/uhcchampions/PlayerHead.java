package me.rexe0.uhcchampions;

import me.rexe0.uhcchampions.skull.Skull;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerHead implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(player.getName());
        meta.setDisplayName(ChatColor.RED+player.getName()+ChatColor.RESET+"'s Head");
        item.setItemMeta(meta);

        player.getWorld().dropItem(player.getLocation(), item);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getItem().getType() != Material.SKULL_ITEM) return;
        ItemStack item = e.getItem();
        Player player = e.getPlayer();

        if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD+"Golden Head")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 320, 1));
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 320, 1));
        }
        player.getInventory().setItemInHand(new ItemStack(Material.AIR));
    }

    public static ShapedRecipe goldenHeadCraft() {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD+"Golden Head");
        item.setItemMeta(meta);
        item.setItemMeta(Skull.getCustomSkull(item, "http://textures.minecraft.net/texture/3bb612eb495ede2c5ca5178d2d1ecf1ca5a255d25dfc3c254bc47f6848791d8"));

        ShapedRecipe recipe = new ShapedRecipe(item);
        recipe.shape("%%%", "%$%", "%%%");
        recipe.setIngredient('%', Material.GOLD_INGOT);
        recipe.setIngredient('$', Material.SKULL_ITEM);
        return recipe;
    }
}
