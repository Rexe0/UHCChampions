package me.rexe0.uhcchampions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class UHCChampions extends JavaPlugin {

    private List<Recipe> recipes;
    private static UHCChampions instance;

    public static UHCChampions getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new PlayerHead(), this);
        getServer().getPluginManager().registerEvents(new Exodus(), this);
        getServer().getPluginManager().registerEvents(new DragonSword(), this);
        getServer().getPluginManager().registerEvents(new AxeOfPerun(), this);
        getServer().getPluginManager().registerEvents(new WarlockPants(), this);
        getServer().getPluginManager().registerEvents(new Cornucopia(), this);
        getServer().getPluginManager().registerEvents(new ChaliceOfGrace(), this);
        getServer().getPluginManager().registerEvents(new DeathScythe(), this);
        getServer().getPluginManager().registerEvents(new AnvilEvent(), this);
        getServer().getPluginManager().registerEvents(new Terminator(), this);
        getServer().getPluginManager().registerEvents(new BlazeSpawn(), this);
        getServer().getPluginManager().registerEvents(new ProjectileIndicator(), this);
        getServer().getPluginManager().registerEvents(new LavaPrevention(), this);
        recipes = new ArrayList<>();
        recipes.add(PlayerHead.goldenHeadCraft());
        recipes.forEach((r) -> getServer().addRecipe(r));

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers())
                    Anduril.andurilCheck(player);
            }
        }.runTaskTimer(this, 20, 10);
    }

    @Override
    public void onDisable() {
        Iterator<Recipe> iterator = getServer().recipeIterator();
        for (Recipe recipe : recipes) {
            ItemStack item = recipe.getResult();
            while (iterator.hasNext()) {
                Recipe rep = iterator.next();
                if (rep == null) continue;
                if (rep.getResult().isSimilar(item)) iterator.remove();
            }
        }
    }
    public static void dealTrueDamage(LivingEntity entity, double amount) {
        dealTrueDamage(entity,amount, null);
    }
    public static void dealTrueDamage(LivingEntity entity, double amount, LivingEntity damager) {
        double health = entity.getHealth();
        health -= amount;
        if (health <= 0) {
            entity.setHealth(1);
            entity.damage(20);
        } else {
            entity.setHealth(health);
            entity.damage(0.001, damager);
        }
    }
    public static boolean isItem(ItemStack item, String name) {
        if (item == null || item.getType() == Material.AIR) return false;
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return false;
        return item.getItemMeta().getDisplayName().equals(name);
    }
}
