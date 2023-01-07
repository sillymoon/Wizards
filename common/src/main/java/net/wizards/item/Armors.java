package net.wizards.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;
import net.wizards.WizardsMod;
import net.wizards.item.armor.WizardArmor;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Armors {
    public static class Material implements ArmorMaterial {
        private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
        private final String name;
        private final int durabilityMultiplier;
        private final int[] protectionAmounts;
        private final int enchantability;
        private final SoundEvent equipSound;
        private final float toughness;
        private final float knockbackResistance;
        private final Lazy<Ingredient> repairIngredientSupplier;

        private Material(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier repairIngredientSupplier) {
            this.name = name;
            this.durabilityMultiplier = durabilityMultiplier;
            this.protectionAmounts = protectionAmounts;
            this.enchantability = enchantability;
            this.equipSound = equipSound;
            this.toughness = toughness;
            this.knockbackResistance = knockbackResistance;
            this.repairIngredientSupplier = new Lazy(repairIngredientSupplier);
        }

        public int getDurability(EquipmentSlot slot) {
            return BASE_DURABILITY[slot.getEntitySlotId()] * this.durabilityMultiplier;
        }

        public int getProtectionAmount(EquipmentSlot slot) {
            return this.protectionAmounts[slot.getEntitySlotId()];
        }

        public int getEnchantability() {
            return this.enchantability;
        }

        public SoundEvent getEquipSound() {
            return this.equipSound;
        }

        public Ingredient getRepairIngredient() {
            return (Ingredient)this.repairIngredientSupplier.get();
        }

        public String getName() {
            return this.name;
        }

        public float getToughness() {
            return this.toughness;
        }

        public float getKnockbackResistance() {
            return this.knockbackResistance;
        }
    }

    public static class ArmorSet<A extends ArmorItem> {
        public A head, chest, legs, feet;
        public ArmorSet(A head, A chest, A legs, A feet) {
            this.head = head;
            this.chest = chest;
            this.legs = legs;
            this.feet = feet;
        }
        public List<A> pieces() {
            return Stream.of(head, chest, legs, feet).filter(Objects::nonNull).collect(Collectors.toList());
        }
        public void register() {
            for (var piece: pieces()) {
                var name = piece.getMaterial().getName() + "_" + piece.getSlotType().getName();
                Registry.register(Registry.ITEM, new Identifier(WizardsMod.ID, name), piece);
            }
        }
    }

    public static Material wizardRobe = new Material(
            "wizard_robe",
            10,
            new int[]{1, 2, 3, 1},
            10,
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
            0,
            0, () -> {
        return Ingredient.ofItems(new ItemConvertible[]{Items.LAPIS_LAZULI});
    });
    public static final ArmorSet<WizardArmor> wizardRobeSet = new ArmorSet(
        new WizardArmor(wizardRobe, EquipmentSlot.HEAD, new Item.Settings().group(Group.WIZARDS)),
        new WizardArmor(wizardRobe, EquipmentSlot.CHEST, new Item.Settings().group(Group.WIZARDS)),
        new WizardArmor(wizardRobe, EquipmentSlot.LEGS, new Item.Settings().group(Group.WIZARDS)),
        new WizardArmor(wizardRobe, EquipmentSlot.FEET, new Item.Settings().group(Group.WIZARDS))
    );

    public static void register() {
        wizardRobeSet.register();
    }
}
