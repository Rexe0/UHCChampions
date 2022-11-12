package me.rexe0.uhcchampions;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DragonSword implements Listener {
    @EventHandler
    public void onHit(CraftItemEvent e) {
        ItemStack result = e.getRecipe().getResult();
        if (!UHCChampions.isItem(result, ChatColor.GREEN+"Dragon Sword")) return;
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN+"Dragon Sword");
        item.setItemMeta(meta);
        e.getInventory().setResult(addAttributes(item));
    }

    private ItemStack addAttributes(ItemStack i){
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(i);
        NBTTagCompound compound = nmsStack.getTag();
        if (compound == null) {
            compound = new NBTTagCompound();
            nmsStack.setTag(compound);
            compound = nmsStack.getTag();
        }
        NBTTagList modifiers = new NBTTagList();
        NBTTagCompound boost = new NBTTagCompound();
        boost.set("AttributeName", new NBTTagString("generic.attackDamage"));
        boost.set("Name", new NBTTagString("generic.attackDamage"));
        boost.set("Amount", new NBTTagInt(8));
        boost.set("Operation", new NBTTagInt(0));
        boost.set("UUIDLeast", new NBTTagInt(894654));
        boost.set("UUIDMost", new NBTTagInt(2872));
        modifiers.add(boost);
        compound.set("AttributeModifiers", modifiers);
        nmsStack.setTag(compound);
        i = CraftItemStack.asBukkitCopy(nmsStack);
        return i;
    }
}
