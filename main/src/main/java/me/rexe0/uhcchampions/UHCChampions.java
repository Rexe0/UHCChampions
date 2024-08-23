package me.rexe0.uhcchampions;

import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.scenarios.Scenario;
import com.gmail.val59000mc.utils.UniversalMaterial;
import me.rexe0.uhcchampions.commands.ViewInventoryCommand;
import me.rexe0.uhcchampions.config.ConfigLoader;
import me.rexe0.uhcchampions.items.Terminator;
import me.rexe0.uhcchampions.items.*;
import me.rexe0.uhcchampions.scenarios.EarlySurfaceListener;
import me.rexe0.uhcchampions.scenarios.GamblersDreamListener;
import me.rexe0.uhcchampions.scenarios.KingFisherListener;
import me.rexe0.uhcchampions.scenarios.SupplyDropsListener;
import me.rexe0.uhcchampions.score.ScoreDisplay;
import me.rexe0.uhcchampions.score.ScoreFile;
import me.rexe0.uhcchampions.score.ScoreListener;
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
    private static ConfigLoader configLoader;

    public static UHCChampions getInstance() {
        return instance;
    }

    public static ConfigLoader getConfigLoader() {
        return configLoader;
    }

    private static final Scenario EARLY_SURFACE = new Scenario("early_surface", UniversalMaterial.GRASS_BLOCK, EarlySurfaceListener.class);
    private static final Scenario GAMBLERS_DREAM = new Scenario("gamblers_dream", UniversalMaterial.END_PORTAL_FRAME, GamblersDreamListener.class);
    private static final Scenario KING_FISHER = new Scenario("king_fisher", UniversalMaterial.PUFFERFISH, KingFisherListener.class);
    private static final Scenario SUPPLY_DROPS = new Scenario("supply_drops", UniversalMaterial.CHEST, SupplyDropsListener.class);

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        ConfigLoader loader = new ConfigLoader(getConfig());
        getCommand("uhcchampionsreload").setExecutor(loader);
        configLoader = loader;

        ScoreFile.fileCheck();
        ScoreFile.loadData();

        // Plugin startup logic
        ViewInventoryCommand viewInventoryCommand = new ViewInventoryCommand();
        getServer().getPluginCommand("viewinventory").setExecutor(viewInventoryCommand);
        getServer().getPluginManager().registerEvents(viewInventoryCommand, this);

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
        getServer().getPluginManager().registerEvents(new MobChanges(), this);
        getServer().getPluginManager().registerEvents(new ProjectileIndicator(), this);
        getServer().getPluginManager().registerEvents(new LavaPrevention(), this);
        getServer().getPluginManager().registerEvents(new CompassTracker(), this);
        getServer().getPluginManager().registerEvents(new VoidGrimoire(), this);
        getServer().getPluginManager().registerEvents(new FlaskOfCleansing(), this);
        getServer().getPluginManager().registerEvents(new FlaskOfIchor(), this);
        getServer().getPluginManager().registerEvents(new LightApple(), this);
        getServer().getPluginManager().registerEvents(new ModularBow(), this);
        getServer().getPluginManager().registerEvents(new StrengthNerf(), this);
        getServer().getPluginManager().registerEvents(new ScoreDisplay(), this);
        getServer().getPluginManager().registerEvents(new ScoreListener(), this);
        getServer().getPluginManager().registerEvents(new ArtemisBow(), this);
        getServer().getPluginManager().registerEvents(new Excalibur(), this);
        getServer().getPluginManager().registerEvents(new DiceOfGod(), this);
        getServer().getPluginManager().registerEvents(new VampireFang(), this);
        getServer().getPluginManager().registerEvents(new MythrilMantle(), this);
        recipes = new ArrayList<>();
        if (UHCChampions.getConfigLoader().isGoldenHeads())
            recipes.add(PlayerHead.goldenHeadCraft());
        recipes.forEach((r) -> getServer().addRecipe(r));

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Anduril.andurilCheck(player);
                    BarbarianChestplate.barbarianCheck(player);
                    CompassTracker.run();
                    FlaskOfCleansing.flaskCheck();
                }
                SpectatorFix.run();
            }
        }.runTaskTimer(this, 20, 10);

        EARLY_SURFACE.setInfo(new Scenario.Info("Early Surface", EarlySurfaceListener.getDescription()));
        GAMBLERS_DREAM.setInfo(new Scenario.Info("Gambler's Dream", GamblersDreamListener.getDescription()));
        KING_FISHER.setInfo(new Scenario.Info("King Fisher", KingFisherListener.getDescription()));
        SUPPLY_DROPS.setInfo(new Scenario.Info("Supply Drops", SupplyDropsListener.getDescription()));
        GameManager.getGameManager().getScenarioManager().registerScenario(EARLY_SURFACE);
        GameManager.getGameManager().getScenarioManager().registerScenario(GAMBLERS_DREAM);
        GameManager.getGameManager().getScenarioManager().registerScenario(KING_FISHER);
        GameManager.getGameManager().getScenarioManager().registerScenario(SUPPLY_DROPS);
    }

    @Override
    public void onDisable() {
        ScoreFile.saveData();

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
        if (entity.isDead()) return;
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
