package me.rexe0.uhcchampions.items;

import com.gmail.val59000mc.customitems.Craft;
import com.gmail.val59000mc.customitems.CraftsManager;
import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.config.ConfigLoader;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class DiceOfGod implements Listener {
    private static final Random random = new Random();
    private static final String[] CRAFT_LIST = new String[]{
            "Book of Thoth",
            "Anduril",
            "Dragon Sword",
            "Dragon Chestplate",
            "Artemis Bow",
            "Nectar",
            "Vitality Potion",
            "Hide of Leviathan",
            "Genesis",
            "Shoes of Vidar",
            "Seven-League Boots",
            "Tablets of Destiny",
            "Exodus",
            "Axe of Perun",
            "Warlock Pants",
            "Cornucopia",
            "Toughness Potion",
            "Holy Water",
            "Cupid's Bow",
            "Chalice of Grace",
            "Death's Scythe",
            "Terminator",
            "Flask of Ichor",
            "Void Grimoire",
            "Flask of Cleansing",
            "Modular Bow",
            "King's Rod",
            "Barbarian Chestplate",
            "Excalibur",
            "Vampire Fang",
            "Mythril Mantle",
    };
    private static final String id = "dice-of-god";
    @EventHandler
    public void onCraft(CraftItemEvent e) {
        ItemStack result = e.getRecipe().getResult();
        ConfigLoader loader = UHCChampions.getConfigLoader();

        if (!UHCChampions.isItem(result, loader.getItemName(id))) return;

        ItemStack item = generateRandomItem();
        if (item == null)
            e.getWhoClicked().sendMessage(ChatColor.RED+"ERROR: Your Dice of God failed to craft.");

        e.getInventory().setResult(item);
    }

    private static ItemStack getRandomItem() {
        String name = CRAFT_LIST[random.nextInt(CRAFT_LIST.length)];

        for (Craft craft : CraftsManager.getCrafts()) {
            ItemStack item = craft.getCraft();
            if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) continue;
            if (!ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals(name)) continue;
            if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals("Dragon Sword"))
                return DragonSword.getItem();
            return item;
        }
        return null;
    }

    public static ItemStack generateRandomItem() {

        int i = 0;
        ItemStack item;
        do {
            item = getRandomItem();
            i++;
        } while (item == null && i < 5);
        return item;
    }
}
