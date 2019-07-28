package com.jozufozu.quench.common.block

import net.minecraft.block.Block
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

class BlockAttuning: Block(Material.ROCK, MapColor.BLUE), ITileEntityProvider {
    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}