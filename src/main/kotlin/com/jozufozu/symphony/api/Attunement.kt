package com.jozufozu.symphony.api

import com.jozufozu.symphony.api.interactions.AttackInteraction
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.LivingEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.INBT
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.living.LivingDropsEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock
import net.minecraftforge.registries.ForgeRegistryEntry

abstract class Attunement(val type: () -> AttunementType<*>): ForgeRegistryEntry<Attunement>() {
    var dirty: Boolean = false

    abstract fun getDisplay(tooltip: MutableList<ITextComponent>, advanced: ITooltipFlag, expand: Boolean)

    /**
     * Whether or not this type of note can be applied to the given item stack.
     * [::attune][AttunementType] will not be called if this returns false.
     */
    abstract fun canBeApplied(stack: ItemStack): Boolean

    abstract fun serializeNBT(): INBT

    /**
     * Do any *additional* modifications here, i.e. enchanting an item.
     */
    open fun attune(stack: ItemStack) {}

    /**
     * Remove any *additional* data here.
     */
    open fun remove(stack: ItemStack) {}

    open fun onUserUpdate(stack: ItemStack, equipmentType: EquipmentSlotType, user: LivingEntity) {}

    open fun onUserAttackedByEntity(stack: ItemStack, equipmentType: EquipmentSlotType, attack: AttackInteraction) {}

    open fun onUserAttackEntity(stack: ItemStack, equipmentType: EquipmentSlotType, attack: AttackInteraction) {}

    open fun onUserDeath(stack: ItemStack, equipmentType: EquipmentSlotType, event: LivingDeathEvent) {}

    open fun onUserKill(stack: ItemStack, equipmentType: EquipmentSlotType, event: LivingDeathEvent) {}

    /**
     * Called when the user of this attunement dies and drops their items.
     *
     * @return true to have this item removed from the drops.
     */
    open fun onUserDrops(stack: ItemStack, event: LivingDropsEvent): Boolean = false

    open fun onBreakBlock(stack: ItemStack, equipmentType: EquipmentSlotType) {}

    open fun onEntityInteract(stack: ItemStack, equipmentType: EquipmentSlotType, event: PlayerInteractEvent.EntityInteractSpecific) {}

    open fun onRightClickBlock(stack: ItemStack, equipmentType: EquipmentSlotType, event: RightClickBlock) {}
}
