package com.jozufozu.symphony.common.block

import net.minecraft.block.Blocks
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler
import java.util.function.Supplier

class TileEntityAttuning: TileEntity(type), ITickableTileEntity {
    private var stacks: IItemHandler = object : ItemStackHandler(12) {
        override fun getSlotLimit(slot: Int) = if (slot == 0) super.getSlotLimit(slot) else 1
    }

    override fun tick() {
    }

    override fun <T : Any?> getCapability(cap: Capability<T>): LazyOptional<T> {
        return if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) LazyOptional.of { stacks }.cast() else LazyOptional.empty()
    }

    companion object {
        val type: TileEntityType<TileEntityAttuning> = TileEntityType.Builder.create<TileEntityAttuning>(Supplier { TileEntityAttuning() }, Blocks.AIR).build(null)
    }
}