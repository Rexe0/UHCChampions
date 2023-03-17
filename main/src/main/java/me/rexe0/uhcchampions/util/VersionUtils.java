package me.rexe0.uhcchampions.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

public abstract class VersionUtils {
    private static VersionUtils versionUtils;

    static {
        try {
            String packageName = VersionUtils.class.getPackage().getName();
            String versionName = "v"+getVersion().replace(".", "_");
            versionUtils = (VersionUtils) Class.forName(packageName + "." + versionName + "." + "VersionUtils_"+versionName).getConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException | InvocationTargetException | NoSuchMethodException exception) {
            Bukkit.getLogger().log(Level.SEVERE, "UHCChampions could not recognize this version");
        }
    }

    public static String getVersion() {
        String ver = Bukkit.getBukkitVersion().split("-")[0];
        return ver.substring(0, ordinalIndexOf(ver, ".", 2)); // Only returns the major and minor version (e.g. 1.8 not 1.8.8)
    }

    private static int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }
    public static VersionUtils getVersionUtils() {
        return versionUtils;
    }



    public abstract ItemStack getItemInMainhand(Player player);

    public abstract void setItemInMainhand(Player player, ItemStack item);


    public abstract double getMaxHealth(Player player);

    public abstract ItemStack getPlayerSkull(Player player);

    public abstract ItemStack getCustomSkull(String url);

    public abstract ItemStack applyEnchantmentTableLogic(ItemStack item, int level);

    public abstract int getDurability(ItemStack item);

    public abstract void setDurability(ItemStack item, int durability);

    public abstract void setRecipeIngredient(ShapedRecipe recipe, char symbol, Material material, short data);
    public abstract ShapedRecipe createRecipe(String id, ItemStack item);

    public abstract ItemStack addAttribute(ItemStack item, Attribute attribute, double amount, int operation, String slot);

    public abstract void spawnBloodParticle(Location location);

    public abstract int getArrowsStuckInBody(LivingEntity entity);

    public abstract void setArrowsStuckInBody(LivingEntity entity, int amount);
}
