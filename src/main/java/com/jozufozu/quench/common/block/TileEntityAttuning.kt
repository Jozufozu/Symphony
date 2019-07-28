package com.jozufozu.quench.common.block

import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler

class TileEntityAttuning: TileEntity(), ITickable {
    private var stacks: IItemHandler = object : ItemStackHandler(12) {
        override fun getSlotLimit(slot: Int) = if (slot == 0) super.getSlotLimit(slot) else 1
    }

    override fun update() {

    }

    override fun <T : Any> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        return if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(stacks) else super.getCapability(capability, facing)
    }
}