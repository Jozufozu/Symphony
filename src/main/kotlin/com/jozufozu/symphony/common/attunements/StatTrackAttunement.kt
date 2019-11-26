package com.jozufozu.symphony.common.attunements

import com.jozufozu.symphony.api.Attunement
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.event.entity.living.LivingDeathEvent

class StatTrackAttunement(val nbt: CompoundNBT): Attunement({ ModAttunements.statTrack }) {
    override fun getDisplay(tooltip: MutableList<ITextComponent>, advanced: ITooltipFlag, expand: Boolean) {

    }

    override fun canBeApplied(stack: ItemStack): Boolean = true

    override fun serializeNBT(): INBT = nbt

    override fun onUserKill(stack: ItemStack, equipmentType: EquipmentSlotType, event: LivingDeathEvent) {
        val entityLiving = event.entityLiving

        val name = entityLiving.type.registryName.toString()

        nbt.putInt(name, nbt.getInt(name) + 1)
    }
}