package com.jozufozu.symphony.common.handlers

import com.jozufozu.symphony.api.SymphonyAPI
import com.jozufozu.symphony.api.interactions.AttackInteraction
import com.jozufozu.symphony.common.attunements.EnchantmentAttunementType
import net.minecraft.entity.LivingEntity
import net.minecraft.potion.EffectInstance
import net.minecraft.util.EntityDamageSource
import net.minecraftforge.event.entity.living.LivingDamageEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import kotlin.math.max

@Mod.EventBusSubscriber
object InteractionEventHandler {

    @SubscribeEvent
    @JvmStatic fun alterTooltip(event: ItemTooltipEvent) {
        val attunements = SymphonyAPI.getStackAttunements(event.itemStack)

        for (attunement in attunements) {
            if (attunement is EnchantmentAttunementType.AttunementEnchantment) {
                event.toolTip.remove(attunement.getEnchantment().getDisplayName(attunement.level))
            }
        }

        event.toolTip.addAll(1, attunements.map { it.getDisplay(false) })
    }

    /**
     * This deals with attacks between two entities. Nothing more
     */
    @SubscribeEvent
    @JvmStatic fun onAttackEntity(event: LivingDamageEvent) {
        val damageSource = event.source
        if (damageSource is ReflectedDamageSource) return

        val attacked = event.entityLiving
        val attacker = damageSource.trueSource ?: return

        val interaction = AttackInteraction(attacked, damageSource, event.amount)

        if (attacker is LivingEntity) {
            SymphonyAPI.getAllAttunements(attacker).forEach { it.onUserAttackEntity(interaction) }
        }

        SymphonyAPI.getAllAttunements(attacked).forEach { it.onUserAttackedByEntity(interaction) }

        val attackedPct = max(1.0f - interaction.reflection, 0.0f)
        val attackerPct = interaction.reflection

        if (attackerPct != 0f) {
            attacker.attackEntityFrom(ReflectedDamageSource(attacked), attackerPct * interaction.damage)

            if (attacker is LivingEntity) {
                interaction.effects.forEach {
                    attacker.addPotionEffect(EffectInstance(it.potion, (it.duration * attackerPct).toInt(), it.amplifier, it.isAmbient, it.doesShowParticles()))
                }
            }
        }

        event.amount = interaction.damage * attackedPct

        if (attackedPct != 0f) {
            interaction.effects.forEach { attacked.addPotionEffect(it) }
        }
    }

}

class ReflectedDamageSource(attacked: LivingEntity) : EntityDamageSource("reflected", attacked) {
    override fun getIsThornsDamage() = true

    override fun isMagicDamage() = true
}
