package com.jozufozu.symphony.api

import com.jozufozu.symphony.api.interactions.AttackInteraction
import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.INBT
import net.minecraft.util.text.ITextComponent

abstract class Attunement() {

    constructor(nbt: INBT): this()

    abstract val colorRGB: Int

    abstract fun getDisplay(advanced: Boolean): ITextComponent

    abstract fun serializeNBT(): INBT

    open fun onUserUpdate(user: LivingEntity) {}

    open fun onUserAttackedByEntity(attack: AttackInteraction) {}

    open fun onUserAttackEntity(attack: AttackInteraction) {}

    open fun onUserDeath() {}
}
