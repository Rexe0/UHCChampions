package me.rexe0.uhcchampions.items;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.config.ConfigLoader;
import me.rexe0.uhcchampions.util.Attribute;
import me.rexe0.uhcchampions.util.VersionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DragonSword implements Listener {
    private static final String id = "dragon-sword";
    @EventHandler
    public void onHit(CraftItemEvent e) {
        ItemStack result = e.getRecipe().getResult();
        ConfigLoader loader = UHCChampions.getConfigLoader();

        if (!UHCChampions.isItem(result, loader.getItemName(id))) return;

        e.getInventory().setResult(getItem());
    }

    public static ItemStack getItem() {
        ConfigLoader loader = UHCChampions.getConfigLoader();

        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Dragon Sword");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Deals "+ChatColor.GREEN+"1"+ChatColor.GRAY+" extra base damage.");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        item = VersionUtils.getVersionUtils().addAttribute(item, Attribute.GENERIC_ATTACK_DAMAGE, loader.getItemDouble(id, "damage"), 0, "HAND");

        if (!VersionUtils.getVersion().equals("1.8"))
            item = VersionUtils.getVersionUtils().addAttribute(item, Attribute.GENERIC_ATTACK_SPEED, -loader.getItemDouble(id, "attack-speed-reduction"), 0, "HAND");
        return item;
    }
}
