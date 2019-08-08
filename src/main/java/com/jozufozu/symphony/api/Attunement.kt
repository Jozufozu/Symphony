package com.jozufozu.symphony.api

import com.jozufozu.symphony.api.interactions.AttackInteraction
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.INBT
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.common.util.INBTSerializable

import java.util.Optional

abstract class Attunement<NBT : INBT> : INBTSerializable<NBT> {

    abstract val colorRGB: Int

    abstract fun getDisplay(advanced: Boolean): ITextComponent

    open fun amplify(): Optional<Attunement<*>> {
        return Optional.empty()
    }

    open fun onUserUpdate(user: LivingEntity) {}

    open fun onUserAttackedByEntity(attack: AttackInteraction) {}

    open fun onUserAttackEntity(attack: AttackInteraction) {}

    open fun onUserDeath() {}
}
