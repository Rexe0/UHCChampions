package me.rexe0.uhcchampions;

import me.rexe0.uhcchampions.skull.Skull;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
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
        if (e.getItem() == null) return;
        if (e.getItem().getType() != Material.SKULL_ITEM) return;
        if (e.getItem().getItemMeta().hasDisplayName()) {
            if (!e.getItem().getItemMeta().getDisplayName().endsWith("Head")) return;
        }
        ItemStack item = e.getItem();
        Player player = e.getPlayer();

        if (!item.getItemMeta().hasDisplayName() || !item.getItemMeta().getDisplayName().equals(ChatColor.GOLD+"Golden Head")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 320, 1));
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 320, 1));
        }
        player.playSound(player.getLocation(), Sound.BURP, 1, 2);
        int amount = item.getAmount();
        amount--;
        if (amount <= 0) {
            player.getInventory().setItemInHand(new ItemStack(Material.AIR));
        } else {
            item.setAmount(amount);
            player.getInventory().setItemInHand(item);
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() != Material.GOLDEN_APPLE) return;
        if (e.getItem().getDurability() != 0) return;
        Player player = e.getPlayer();

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2), true);
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
        recipe.setIngredient('$', Material.SKULL_ITEM, 3);
        return recipe;
    }
}
