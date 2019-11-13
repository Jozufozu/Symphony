package com.jozufozu.symphony.api

import com.jozufozu.symphony.api.interactions.AttackInteraction
import net.minecraft.entity.LivingEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.registries.ForgeRegistryEntry

abstract class Attunement(val type: AttunementType<*>): ForgeRegistryEntry<Attunement>() {
    var dirty: Boolean = false

    abstract fun getDisplay(advanced: Boolean): ITextComponent

    /**
     * Whether or not this type of note can be applied to the given item stack.
     * [::attune][AttunementType] will not be called if this returns false.
     */
    abstract fun canBeApplied(stack: ItemStack): Boolean

    /**
     * Do any *additional* modifications here, i.e. enchanting an item.
     */
    open fun attune(stack: ItemStack) {}

    /**
     * Remove any *additional* data here.
     */
    open fun remove(stack: ItemStack) {}

    open fun serializeNBT(): INBT = CompoundNBT()

    open fun onUserUpdate(stack: ItemStack, equipmentType: EquipmentSlotType, user: LivingEntity) {}

    open fun onUserAttackedByEntity(stack: ItemStack, equipmentType: EquipmentSlotType, attack: AttackInteraction) {}

    open fun onUserAttackEntity(stack: ItemStack, equipmentType: EquipmentSlotType, attack: AttackInteraction) {}

    open fun onUserDeath(stack: ItemStack, equipmentType: EquipmentSlotType) {}

    open fun breakBlock(stack: ItemStack, equipmentType: EquipmentSlotType) {}

    open fun rightClickEntity(stack: ItemStack, equipmentType: EquipmentSlotType) {}
}
