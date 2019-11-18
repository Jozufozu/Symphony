package com.jozufozu.symphony.common.attunements

import com.jozufozu.symphony.api.Attunement
import net.minecraft.block.Blocks
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.LivingEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.IntNBT
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.event.entity.player.PlayerInteractEvent

class LightCollectorAttunement(var charges: Int): Attunement({ ModAttunements.coalTorch }) {
    constructor(nbt: IntNBT): this(nbt.int)
    constructor(): this(0)

    override fun canBeApplied(stack: ItemStack) = true

    override fun getDisplay(tooltip: MutableList<ITextComponent>, advanced: ITooltipFlag, expand: Boolean) {
        tooltip.add(StringTextComponent("$charges"))
    }

    override fun serializeNBT() = IntNBT(charges)

    override fun onUserUpdate(stack: ItemStack, equipmentType: EquipmentSlotType, user: LivingEntity) {
        val pos = user.position

        if (user.world.canBlockSeeSky(pos) && user.world.rand.nextDouble() < 0.005) {
            ++charges
            dirty = true
        }
    }

    override fun onRightClickBlock(stack: ItemStack, equipmentType: EquipmentSlotType, event: PlayerInteractEvent.RightClickBlock) {
        if (charges > 0) {
            --charges

            event.world.setBlockState(event.pos.offset(event.face!!), Blocks.TORCH.defaultState)
            dirty = true
        }
    }
}