package com.jozufozu.symphony.common.attunements

import com.jozufozu.symphony.api.Attunement
import com.jozufozu.symphony.api.interactions.AttackInteraction
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.IntNBT
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent

class BuildUpAttunement(var hits: Int = 0) : Attunement() {
    constructor(nbt: IntNBT): this(nbt.int)

    override val name: ResourceLocation
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun getDisplay(advanced: Boolean): ITextComponent {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun canBeApplied(stack: ItemStack) = true

    override fun serializeNBT() = IntNBT(hits)

    override fun onUserAttackEntity(equipmentType: EquipmentSlotType, attack: AttackInteraction) {
        if (++hits >= 3) {
            attack.damage *= 1.3f
            hits = 0
        }
        dirty = true
    }
}