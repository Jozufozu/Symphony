package com.jozufozu.symphony.common.attunements

import com.jozufozu.symphony.api.Attunement
import com.jozufozu.symphony.api.interactions.AttackInteraction
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.IntNBT
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextFormatting

class BuildUpAttunement(var hits: Int) : Attunement({ ModAttunements.buildUp }) {
    constructor(nbt: IntNBT): this(nbt.int)
    constructor(): this(0)

    override fun getDisplay(tooltip: MutableList<ITextComponent>, advanced: ITooltipFlag, expand: Boolean) {
        tooltip.add(when (hits) {
            in 0..2 -> StringTextComponent("Charging: $hits/4")
            3 -> StringTextComponent("Charged").setStyle(Style().setColor(TextFormatting.RED))
            else -> StringTextComponent("hetaonsu")
        })
    }

    override fun canBeApplied(stack: ItemStack) = true

    override fun serializeNBT() = IntNBT(hits)

    override fun onUserAttackEntity(stack: ItemStack, equipmentType: EquipmentSlotType, attack: AttackInteraction) {
        if (++hits >= 4) {
            attack.damage *= 4.3f
            hits = 0
        } else {
            attack.damage *= 0.1f
        }
        dirty = true
    }
}