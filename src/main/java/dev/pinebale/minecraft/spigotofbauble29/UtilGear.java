package dev.pinebale.minecraft.spigotofbauble29;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UtilGear {

    private static final Set<Material> ARMOR = EnumSet.of(
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
            Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS,
            Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
            Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS
    );

    private static final Set<Material> AXES = EnumSet.of(
            Material.WOOD_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE
    );

    private static final Set<Material> SWORDS = EnumSet.of(
            Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD
    );

    private static final Set<Material> SHOVELS = EnumSet.of(
            Material.WOOD_SPADE, Material.STONE_SPADE, Material.IRON_SPADE, Material.GOLD_SPADE, Material.DIAMOND_SPADE
    );

    private static final Set<Material> HOES = EnumSet.of(
            Material.WOOD_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLD_HOE, Material.DIAMOND_HOE
    );

    private static final Set<Material> PICKAXES = EnumSet.of(
            Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE
    );

    private static final Set<Material> DIAMOND_GEAR = EnumSet.of(
            Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_SPADE, Material.DIAMOND_HOE, Material.DIAMOND_PICKAXE,
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS
    );

    private static final Set<Material> GOLD_GEAR = EnumSet.of(
            Material.GOLD_SWORD, Material.GOLD_AXE, Material.GOLD_SPADE, Material.GOLD_HOE, Material.GOLD_PICKAXE,
            Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS
    );

    public static boolean isArmor(ItemStack item) {
        return item != null && ARMOR.contains(item.getType());
    }

    public static boolean isAxe(ItemStack item) {
        return item != null && AXES.contains(item.getType());
    }

    public static boolean isSword(ItemStack item) {
        return item != null && SWORDS.contains(item.getType());
    }

    public static boolean isShovel(ItemStack item) {
        return item != null && SHOVELS.contains(item.getType());
    }

    public static boolean isHoe(ItemStack item) {
        return item != null && HOES.contains(item.getType());
    }

    public static boolean isPickaxe(ItemStack item) {
        return item != null && PICKAXES.contains(item.getType());
    }

    public static boolean isDiamond(ItemStack item) {
        return item != null && DIAMOND_GEAR.contains(item.getType());
    }

    public static boolean isGold(ItemStack item) {
        return item != null && GOLD_GEAR.contains(item.getType());
    }

    public static boolean isBow(ItemStack item) {
        return item != null && item.getType().equals(Material.BOW);
    }

    public static boolean isWeapon(ItemStack item) {
        return isAxe(item) || isSword(item);
    }

    public static boolean isMaterial(ItemStack item, Material material) {
        return item != null && item.getType().equals(material);
    }

    public static boolean isMaterialAndData(ItemStack item, Material material, byte data) {
        return item != null && item.getType().equals(material) && item.getDurability() == (data & 0xFF);
    }

    public static boolean isRepairable(ItemStack item) {
        return item != null && item.getType().getMaxDurability() > 0;
    }
}
