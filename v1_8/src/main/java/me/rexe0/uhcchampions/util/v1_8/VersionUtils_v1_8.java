package me.rexe0.uhcchampions.util.v1_8;

import me.rexe0.uhcchampions.util.Attribute;
import me.rexe0.uhcchampions.util.EnchantmentType;
import me.rexe0.uhcchampions.util.VersionUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class VersionUtils_v1_8 extends VersionUtils {
    private Random random = new Random();
    @Override
    public ItemStack getItemInMainhand(Player player) {
        return player.getItemInHand();
    }

    @Override
    public void setItemInMainhand(Player player, ItemStack item) {
        player.setItemInHand(item);
    }

    @Override
    public double getMaxHealth(Player player) {
        return player.getMaxHealth();
    }

    @Override
    public ItemStack getPlayerSkull(Player player) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(player.getName());
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemStack getCustomSkull(String url) {
        return Skull_v1_8.getCustomSkull(new ItemStack(Material.SKULL_ITEM, 1, (short) 3), url);
    }

    @Override
    public ItemStack applyEnchantmentTableLogic(ItemStack item, int level) {
        return CraftItemStack.asBukkitCopy(EnchantmentManager.a(new Random(), CraftItemStack.asNMSCopy(item), level));
    }

    @Override
    public int getDurability(ItemStack item) {
        return item.getDurability();
    }

    @Override
    public void setDurability(ItemStack item, int durability) {
        item.setDurability((short) durability);
    }

    @Override
    public void setRecipeIngredient(ShapedRecipe recipe, char symbol, Material material, short data) {
        recipe.setIngredient(symbol, material, data);
    }

    @Override
    public ShapedRecipe createRecipe(String id, ItemStack item) {
        return new ShapedRecipe(item);
    }

    @Override
    public ItemStack addAttribute(ItemStack item, Attribute attribute, double amount, int operation, String slot) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = nmsStack.getTag();
        if (compound == null) {
            compound = new NBTTagCompound();
            nmsStack.setTag(compound);
            compound = nmsStack.getTag();
        }
        NBTTagList modifiers = new NBTTagList();
        NBTTagCompound boost = new NBTTagCompound();
        boost.set("AttributeName", new NBTTagString(attribute.getName()));
        boost.set("Name", new NBTTagString(attribute.getName()));
        boost.set("Amount", new NBTTagFloat((float) amount));
        boost.set("Operation", new NBTTagInt(operation));
        if (slot != null)
            boost.set("Slot", new NBTTagString(slot));
        boost.set("UUIDLeast", new NBTTagLong(random.nextLong()));
        boost.set("UUIDMost", new NBTTagLong(random.nextLong()));
        modifiers.add(boost);
        compound.set("AttributeModifiers", modifiers);
        nmsStack.setTag(compound);
        item = CraftItemStack.asBukkitCopy(nmsStack);
        return item;
    }

    @Override
    public void spawnBloodParticle(Location location) {
        PacketPlayOutWorldParticles particlePacket = new PacketPlayOutWorldParticles(EnumParticle.BLOCK_CRACK, true, (float) location.getX(), (float) (location.getY()), (float) location.getZ(), 0.5f, 0.5f, 0.5f, 0f, 200, 152);

        for (Player p : location.getWorld().getPlayers())
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(particlePacket);
    }

    @Override
    public int getArrowsStuckInBody(LivingEntity entity) {
        return ((CraftLivingEntity) entity).getHandle().getDataWatcher().getByte(9);
    }

    @Override
    public void setArrowsStuckInBody(LivingEntity entity, int amount) {
        ((CraftLivingEntity)entity).getHandle().getDataWatcher().watch(9, (byte) 0);
    }

    @Override
    public PotionEffectType getPotionEffectType(me.rexe0.uhcchampions.util.PotionEffectType type) {
        switch (type) {
            case SPEED: return PotionEffectType.SPEED;
            case SLOWNESS: return PotionEffectType.SLOW;
            case HASTE: return PotionEffectType.FAST_DIGGING;
            case MINING_FATIGUE: return PotionEffectType.SLOW_DIGGING;
            case STRENGTH: return PotionEffectType.INCREASE_DAMAGE;
            case INSTANT_HEALTH: return PotionEffectType.HEAL;
            case INSTANT_DAMAGE: return PotionEffectType.HARM;
            case JUMP_BOOST: return PotionEffectType.JUMP;
            case NAUSEA: return PotionEffectType.CONFUSION;
            case REGENERATION: return PotionEffectType.REGENERATION;
            case RESISTANCE: return PotionEffectType.DAMAGE_RESISTANCE;
            case FIRE_RESISTANCE: return PotionEffectType.FIRE_RESISTANCE;
            case WATER_BREATHING: return PotionEffectType.WATER_BREATHING;
            case INVISIBILITY: return PotionEffectType.INVISIBILITY;
            case BLINDNESS: return PotionEffectType.BLINDNESS;
            case NIGHT_VISION: return PotionEffectType.NIGHT_VISION;
            case HUNGER: return PotionEffectType.HUNGER;
            case WEAKNESS: return PotionEffectType.WEAKNESS;
            case POISON: return PotionEffectType.POISON;
            case WITHER: return PotionEffectType.WITHER;
            case HEALTH_BOOST: return PotionEffectType.HEALTH_BOOST;
            case ABSORPTION: return PotionEffectType.ABSORPTION;
            case SATURATION: return PotionEffectType.SATURATION;
            default: return null;
        }
    }
    @Override
    public Enchantment getEnchantmentType(EnchantmentType type) {
        switch (type) {
            case PROTECTION: return Enchantment.PROTECTION_ENVIRONMENTAL;
            case FIRE_PROTECTION: return Enchantment.PROTECTION_FIRE;
            case FEATHER_FALLING: return Enchantment.PROTECTION_FALL;
            case BLAST_PROTECTION: return Enchantment.PROTECTION_EXPLOSIONS;
            case PROJECTILE_PROTECTION: return Enchantment.PROTECTION_PROJECTILE;
            case RESPIRATION: return Enchantment.OXYGEN;
            case AQUA_AFFINITY: return Enchantment.WATER_WORKER;
            case THORNS: return Enchantment.THORNS;
            case DEPTH_STRIDER: return Enchantment.DEPTH_STRIDER;
            case SHARPNESS: return Enchantment.DAMAGE_ALL;
            case SMITE: return Enchantment.DAMAGE_UNDEAD;
            case BANE_OF_ARTHROPODS: return Enchantment.DAMAGE_ARTHROPODS;
            case KNOCKBACK: return Enchantment.KNOCKBACK;
            case FIRE_ASPECT: return Enchantment.FIRE_ASPECT;
            case LOOTING: return Enchantment.LOOT_BONUS_MOBS;
            case EFFICIENCY: return Enchantment.DIG_SPEED;
            case SILK_TOUCH: return Enchantment.SILK_TOUCH;
            case UNBREAKING: return Enchantment.DURABILITY;
            case FORTUNE: return Enchantment.LOOT_BONUS_BLOCKS;
            case POWER: return Enchantment.ARROW_DAMAGE;
            case PUNCH: return Enchantment.ARROW_KNOCKBACK;
            case FLAME: return Enchantment.ARROW_FIRE;
            case INFINITY: return Enchantment.ARROW_INFINITE;
            case LUCK_OF_THE_SEA: return Enchantment.LUCK;
            case LURE: return Enchantment.LURE;
            default: return null;
        }
    }
}
