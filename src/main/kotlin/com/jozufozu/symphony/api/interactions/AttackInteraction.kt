package com.jozufozu.symphony.api.interactions

import com.google.common.collect.Lists
import net.minecraft.entity.LivingEntity
import net.minecraft.potion.EffectInstance
import net.minecraft.util.DamageSource

class AttackInteraction(attacked: LivingEntity, var damageSource: DamageSource?) {
    /**
     * The [LivingEntity] that initiated the attack
     */
    var attacker: LivingEntity? = null
        private set

    /**
     * A list of [LivingEntity]s who will feel this attack.<br></br>
     * Typically only one
     */
    val entities: List<LivingEntity>
    val effects: List<EffectInstance>

    var damage: Float = 0f
    var knockback: Float = 0f

    var fireTime: Int = 0

    /**
     * A number greater than 0.0 that represents who the damage and effects go to.<br></br>
     * 0.0 means 100 percent of the effects are felt by the [AttackInteraction.entities]<br></br>
     * 1.0 means 100 percent of the effects are reflected back at the [AttackInteraction.attacker]<br></br>
     * 2.0 means the damage and other effects are doubled and reflected back at the [AttackInteraction.attacker]
     */
    var reflection: Float = 0f

    init {
        this.entities = Lists.newArrayList(attacked)
        this.effects = Lists.newArrayList()

        if (damageSource?.trueSource is LivingEntity)
            this.attacker = damageSource?.trueSource as LivingEntity
    }

    constructor(attacked: LivingEntity, damageSource: DamageSource, damage: Float) : this(attacked, damageSource) {
        this.damage = damage
    }
}
