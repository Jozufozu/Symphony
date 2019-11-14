package com.jozufozu.symphony.common.attunements

import com.jozufozu.symphony.api.Attunement
import net.minecraft.block.Blocks
import net.minecraft.entity.item.ItemEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.IntNBT
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.event.entity.player.PlayerInteractEvent

class CoalTorchAttunement(var charges: Int): Attunement({ ModAttunements.coalTorch }) {
    constructor(nbt: IntNBT): this(nbt.int)
    constructor(): this(0)

    override fun canBeApplied(stack: ItemStack) = true

    override fun getDisplay(advanced: Boolean) = StringTextComponent("$charges")

    override fun serializeNBT() = IntNBT(charges)

    override fun onEntityInteract(stack: ItemStack, equipmentType: EquipmentSlotType, event: PlayerInteractEvent.EntityInteract) {
        val entity = event.target
        if (entity is ItemEntity) {
            val entityItem = entity.item

            if (entityItem.item === Items.COAL || entityItem.item === Items.CHARCOAL) {
                entityItem.shrink(1)
                charges++
                dirty = true
            }
        }
    }

    override fun onRightClickBlock(stack: ItemStack, equipmentType: EquipmentSlotType, event: PlayerInteractEvent.RightClickBlock) {
        if (charges > 0) {
            --charges

            event.world.setBlockState(event.pos.offset(event.face!!), Blocks.TORCH.defaultState)
        }
    }
}