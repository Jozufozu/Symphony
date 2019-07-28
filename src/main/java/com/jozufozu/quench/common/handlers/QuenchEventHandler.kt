package com.jozufozu.quench.common.handlers

import com.jozufozu.quench.api.SymphonyAPI
import com.jozufozu.quench.api.interactions.AttackInteraction
import com.jozufozu.quench.common.EnchantmentNoteType
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.PotionEffect
import net.minecraft.util.EntityDamageSource
import net.minecraftforge.event.entity.living.LivingDamageEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.max

@Mod.EventBusSubscriber
object QuenchEventHandler {

    @SubscribeEvent
    @JvmStatic fun alterTooltip(event: ItemTooltipEvent) {
        val attunements = SymphonyAPI.getStackAttunements(event.itemStack)

        for (attunement in attunements) {
            if (attunement is EnchantmentNoteType.AttunementEnchantment) {
                event.toolTip.remove(attunement.getEnchantment().getTranslatedName(attunement.level))
            }
        }

        event.toolTip.addAll(1, attunements.map { it.getDisplay(false).formattedText })
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

        if (attacker is EntityLivingBase) {
            SymphonyAPI.getAllAttunements(attacker).forEach { it.onUserAttackEntity(interaction) }
        }

        SymphonyAPI.getAllAttunements(attacked).forEach { it.onUserAttackedByEntity(interaction) }

        val attackedPct = max(1.0f - interaction.reflection, 0.0f)
        val attackerPct = interaction.reflection

        if (attackerPct != 0f) {
            attacker.attackEntityFrom(ReflectedDamageSource(attacked), attackerPct * interaction.damage)

            if (attacker is EntityLivingBase) {
                interaction.effects
                        .map { PotionEffect(it.potion, (it.duration * attackerPct).toInt(), it.amplifier, it.isAmbient, it.doesShowParticles()) }
                        .forEach(attacker::addPotionEffect)
            }
        }

        event.amount = interaction.damage * attackedPct

        if (attackedPct != 0f) {
            interaction.effects.forEach(attacked::addPotionEffect)


        }
    }

}

class ReflectedDamageSource(attacked: EntityLivingBase) : EntityDamageSource("reflected", attacked) {
    override fun getIsThornsDamage() = true

    override fun isMagicDamage() = true
}
