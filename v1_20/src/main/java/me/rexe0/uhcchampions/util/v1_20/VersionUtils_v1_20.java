package me.rexe0.uhcchampions.util.v1_20;

import me.rexe0.uhcchampions.UHCChampions;
import me.rexe0.uhcchampions.util.EnchantmentType;
import me.rexe0.uhcchampions.util.VersionUtils;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R4.util.RandomSourceWrapper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;
import java.util.UUID;

public class VersionUtils_v1_20 extends VersionUtils {
    @Override
    public ItemStack getItemInMainhand(Player player) {
        return player.getEquipment().getItemInMainHand();
    }

    @Override
    public void setItemInMainhand(Player player, ItemStack item) {
        player.getEquipment().setItemInMainHand(item);
    }

    @Override
    public double getMaxHealth(Player player) {
        return player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
    }

    @Override
    public double getAbsorptionAmount(Player player) {
        return player.getAbsorptionAmount();
    }

    @Override
    public PotionEffect getPotionEffect(LivingEntity entity, me.rexe0.uhcchampions.util.PotionEffectType type) {
        return entity.getPotionEffect(getPotionEffectType(type));
    }

    @Override
    public ItemStack getPlayerSkull(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemStack getCustomSkull(String url) {
        return Skull_v1_20.getCustomSkull(new ItemStack(Material.PLAYER_HEAD, 1), url);
    }

    @Override
    public ItemStack applyEnchantmentTableLogic(ItemStack item, int level) {

        return CraftItemStack.asBukkitCopy(EnchantmentHelper.enchantItem(FeatureFlagSet.of(), new RandomSourceWrapper(new Random()), CraftItemStack.asNMSCopy(item), level, false));
    }

    @Override
    public int getDurability(ItemStack item) {
        if (!(item.getItemMeta() instanceof Damageable)) return 0;
        return ((Damageable)item.getItemMeta()).getDamage();
    }

    @Override
    public void setDurability(ItemStack item, int durability) {
        if (!(item.getItemMeta() instanceof Damageable)) return;
        Damageable meta = (Damageable) item.getItemMeta();
        meta.setDamage(durability);
        item.setItemMeta(meta);
    }

    @Override
    public void setRecipeIngredient(ShapedRecipe recipe, char symbol, Material material, short data) {
        recipe.setIngredient(symbol, material);
    }

    @Override
    public ShapedRecipe createRecipe(String id, ItemStack item) {
        return new ShapedRecipe(new NamespacedKey(UHCChampions.getInstance(), id), item);
    }

    @Override
    public ItemStack addAttribute(ItemStack item, me.rexe0.uhcchampions.util.Attribute attribute, double amount, int operation, String slot) {
        ItemMeta meta = item.getItemMeta();
        AttributeModifier.Operation operationEnum;
        switch (operation) {
            case 1:
                operationEnum = AttributeModifier.Operation.ADD_SCALAR;
                break;
            case 2:
                operationEnum = AttributeModifier.Operation.MULTIPLY_SCALAR_1;
                break;
            default:
                operationEnum = AttributeModifier.Operation.ADD_NUMBER;
                break;
        }
        EquipmentSlot equipmentSlot = null;
        if (slot != null) equipmentSlot = EquipmentSlot.valueOf(slot);

        meta.addAttributeModifier(Attribute.valueOf(attribute.name()), new AttributeModifier(UUID.randomUUID(), attribute.getName(), amount, operationEnum, equipmentSlot));

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void spawnBloodParticle(Location location) {
        location.getWorld().spawnParticle(Particle.BLOCK, location, 200, 0.5, 0.5, 0.5, Material.REDSTONE_BLOCK.createBlockData());
    }

    @Override
    public void spawnExplosionParticle(Location location) {
        location.getWorld().spawnParticle(Particle.EXPLOSION, location, 1, 0.25, 0.25, 0.25, 0);
    }

    @Override
    public int getArrowsStuckInBody(LivingEntity entity) {
        return entity.getArrowsInBody();
    }

    @Override
    public void setArrowsStuckInBody(LivingEntity entity, int amount) {
        entity.setArrowsInBody(amount);
    }

    @Override
    public PotionEffectType getPotionEffectType(me.rexe0.uhcchampions.util.PotionEffectType type) {
        return switch (type) {
            case SPEED -> PotionEffectType.SPEED;
            case SLOWNESS -> PotionEffectType.SLOWNESS;
            case HASTE -> PotionEffectType.HASTE;
            case MINING_FATIGUE -> PotionEffectType.MINING_FATIGUE;
            case STRENGTH -> PotionEffectType.STRENGTH;
            case INSTANT_HEALTH -> PotionEffectType.INSTANT_HEALTH;
            case INSTANT_DAMAGE -> PotionEffectType.INSTANT_DAMAGE;
            case JUMP_BOOST -> PotionEffectType.JUMP_BOOST;
            case NAUSEA -> PotionEffectType.NAUSEA;
            case REGENERATION -> PotionEffectType.REGENERATION;
            case RESISTANCE -> PotionEffectType.RESISTANCE;
            case FIRE_RESISTANCE -> PotionEffectType.FIRE_RESISTANCE;
            case WATER_BREATHING -> PotionEffectType.WATER_BREATHING;
            case INVISIBILITY -> PotionEffectType.INVISIBILITY;
            case BLINDNESS -> PotionEffectType.BLINDNESS;
            case NIGHT_VISION -> PotionEffectType.NIGHT_VISION;
            case HUNGER -> PotionEffectType.HUNGER;
            case WEAKNESS -> PotionEffectType.WEAKNESS;
            case POISON -> PotionEffectType.POISON;
            case WITHER -> PotionEffectType.WITHER;
            case HEALTH_BOOST -> PotionEffectType.HEALTH_BOOST;
            case ABSORPTION -> PotionEffectType.ABSORPTION;
            case SATURATION -> PotionEffectType.SATURATION;
            case GLOWING -> PotionEffectType.GLOWING;
            case LEVITATION -> PotionEffectType.LEVITATION;
            case LUCK -> PotionEffectType.LUCK;
            case UNLUCK -> PotionEffectType.UNLUCK;
            case SLOW_FALLING -> PotionEffectType.SLOW_FALLING;
            case CONDUIT_POWER -> PotionEffectType.CONDUIT_POWER;
            case DOLPHINS_GRACE -> PotionEffectType.DOLPHINS_GRACE;
            case BAD_OMEN -> PotionEffectType.BAD_OMEN;
            case HERO_OF_THE_VILLAGE -> PotionEffectType.HERO_OF_THE_VILLAGE;
            case DARKNESS -> PotionEffectType.DARKNESS;
            case TRIAL_OMEN -> PotionEffectType.TRIAL_OMEN;
            case RAID_OMEN -> PotionEffectType.RAID_OMEN;
            case WIND_CHARGED -> PotionEffectType.WIND_CHARGED;
            case WEAVING -> PotionEffectType.WEAVING;
            case OOZING -> PotionEffectType.OOZING;
            case INFESTED -> PotionEffectType.INFESTED;
            default -> null;
        };
    }
    @Override
    public Enchantment getEnchantmentType(EnchantmentType type) {
        return switch (type) {
            case PROTECTION -> Enchantment.PROTECTION;
            case FIRE_PROTECTION -> Enchantment.FIRE_PROTECTION;
            case FEATHER_FALLING -> Enchantment.FEATHER_FALLING;
            case BLAST_PROTECTION -> Enchantment.BLAST_PROTECTION;
            case PROJECTILE_PROTECTION -> Enchantment.PROJECTILE_PROTECTION;
            case RESPIRATION -> Enchantment.RESPIRATION;
            case AQUA_AFFINITY -> Enchantment.AQUA_AFFINITY;
            case THORNS -> Enchantment.THORNS;
            case DEPTH_STRIDER -> Enchantment.DEPTH_STRIDER;
            case FROST_WALKER -> Enchantment.FROST_WALKER;
            case BINDING_CURSE -> Enchantment.BINDING_CURSE;
            case SHARPNESS -> Enchantment.SHARPNESS;
            case SMITE -> Enchantment.SMITE;
            case BANE_OF_ARTHROPODS -> Enchantment.BANE_OF_ARTHROPODS;
            case KNOCKBACK -> Enchantment.KNOCKBACK;
            case FIRE_ASPECT -> Enchantment.FIRE_ASPECT;
            case LOOTING -> Enchantment.LOOTING;
            case SWEEPING_EDGE -> Enchantment.SWEEPING_EDGE;
            case EFFICIENCY -> Enchantment.EFFICIENCY;
            case SILK_TOUCH -> Enchantment.SILK_TOUCH;
            case UNBREAKING -> Enchantment.UNBREAKING;
            case FORTUNE -> Enchantment.FORTUNE;
            case POWER -> Enchantment.POWER;
            case PUNCH -> Enchantment.PUNCH;
            case FLAME -> Enchantment.FLAME;
            case INFINITY -> Enchantment.INFINITY;
            case LUCK_OF_THE_SEA -> Enchantment.LUCK_OF_THE_SEA;
            case LURE -> Enchantment.LURE;
            case LOYALTY -> Enchantment.LOYALTY;
            case IMPALING -> Enchantment.IMPALING;
            case RIPTIDE -> Enchantment.RIPTIDE;
            case CHANNELING -> Enchantment.CHANNELING;
            case MULTISHOT -> Enchantment.MULTISHOT;
            case QUICK_CHARGE -> Enchantment.QUICK_CHARGE;
            case PIERCING -> Enchantment.PIERCING;
            case MENDING -> Enchantment.MENDING;
            case VANISHING_CURSE -> Enchantment.VANISHING_CURSE;
            case SOUL_SPEED -> Enchantment.SOUL_SPEED;
            case SWIFT_SNEAK -> Enchantment.SWIFT_SNEAK;
            default -> null;
        };
    }
}
