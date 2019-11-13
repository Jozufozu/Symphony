package com.jozufozu.symphony.common.attunements

import com.jozufozu.symphony.api.Attunement
import com.jozufozu.symphony.api.AttunementType
import com.jozufozu.symphony.api.interactions.AttackInteraction
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.IntNBT
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextFormatting

class BuildUpAttunement(type: AttunementType<*>, var hits: Int) : Attunement(type) {
    constructor(type: AttunementType<*>, nbt: IntNBT): this(type, nbt.int)
    constructor(type: AttunementType<*>): this(type, 0)

    override fun getDisplay(advanced: Boolean): ITextComponent {
        return when (hits) {
            in 0..2 -> StringTextComponent("Charging: $hits/3")
            3 -> StringTextComponent("Charged").setStyle(Style().setColor(TextFormatting.RED))
            else -> StringTextComponent("hetaonsu")
        }
    }

    override fun canBeApplied(stack: ItemStack) = true

    override fun serializeNBT() = IntNBT(hits)

    override fun onUserAttackEntity(stack: ItemStack, equipmentType: EquipmentSlotType, attack: AttackInteraction) {
        if (++hits >= 3) {
            attack.damage *= 1.3f
            hits = 0
        }
        dirty = true
    }
}