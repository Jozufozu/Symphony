package com.jozufozu.symphony.common.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.world.IBlockReader

class BlockAttuning: Block(Properties.create(Material.ROCK)) {
    override fun createTileEntity(state: BlockState?, world: IBlockReader?) = TileEntityAttuning()
}