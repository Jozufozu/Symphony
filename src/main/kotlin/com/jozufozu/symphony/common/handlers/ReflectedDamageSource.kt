package com.jozufozu.symphony.common.handlers

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.util.EntityDamageSource

class ReflectedDamageSource(attacked: LivingEntity) : EntityDamageSource("reflected", attacked as Entity) {
    override fun getIsThornsDamage() = true

    override fun isMagicDamage() = true
}