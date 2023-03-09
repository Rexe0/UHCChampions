package me.rexe0.uhcchampions.util.v1_19;

import me.rexe0.uhcchampions.util.VersionUtils;
import net.minecraft.world.item.enchantment.EnchantmentManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_19_R1.util.RandomSourceWrapper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Random;
import java.util.UUID;

public class VersionUtils_v1_19 extends VersionUtils {
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
    public ItemStack getPlayerSkull(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemStack getCustomSkull(String url) {
        return Skull_v1_19.getCustomSkull(new ItemStack(Material.PLAYER_HEAD, 1), url);
    }

    @Override
    public ItemStack applyEnchantmentTableLogic(ItemStack item, int level) {

        return CraftItemStack.asBukkitCopy(EnchantmentManager.a(new RandomSourceWrapper(new Random()), CraftItemStack.asNMSCopy(item), level, false));
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
        location.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 200, 0.5, 0.5, 0.5, Material.REDSTONE_BLOCK.createBlockData());
    }

    @Override
    public int getArrowsStuckInBody(LivingEntity entity) {
        return entity.getArrowsInBody();
    }

    @Override
    public void setArrowsStuckInBody(LivingEntity entity, int amount) {
        entity.setArrowsInBody(amount);
    }
}
