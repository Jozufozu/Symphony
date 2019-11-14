package com.jozufozu.symphony.common.handlers

import com.jozufozu.symphony.api.Attunement
import com.jozufozu.symphony.api.SymphonyAPI
import com.jozufozu.symphony.api.interactions.AttackInteraction
import com.jozufozu.symphony.common.attunements.EnchantmentAttunementType
import net.alexwells.kottle.KotlinEventBusSubscriber
import net.minecraft.entity.LivingEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.potion.EffectInstance
import net.minecraftforge.event.entity.living.LivingDamageEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.living.LivingDropsEvent
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import kotlin.math.ceil
import kotlin.math.max

@KotlinEventBusSubscriber(bus = KotlinEventBusSubscriber.Bus.FORGE)
object InteractionEventHandler {

    @SubscribeEvent
    fun alterTooltip(event: ItemTooltipEvent) {
        val attunements = SymphonyAPI.getStackAttunements(event.itemStack)

        for (attunement in attunements) {
            if (attunement is EnchantmentAttunementType.EnchantmentAttunement) {
                event.toolTip.remove(attunement.getEnchantment().getDisplayName(attunement.level))
            }
        }

        event.toolTip.addAll(1, attunements.map { it.getDisplay(false) })
    }

    @SubscribeEvent
    fun onEntityInteract(event: PlayerInteractEvent.EntityInteract) {
        SymphonyAPI.runOnAllAttunements(event.player) { stack: ItemStack, equipmentType: EquipmentSlotType, attunement: Attunement ->
            attunement.onEntityInteract(stack, equipmentType, event)

            if (attunement.dirty) {
                SymphonyAPI.updateAttunement(stack, attunement)
            }
        }
    }

    @SubscribeEvent
    fun onRightClickBlock(event: PlayerInteractEvent.RightClickBlock) {
        SymphonyAPI.runOnAllAttunements(event.player) { stack: ItemStack, equipmentType: EquipmentSlotType, attunement: Attunement ->
            attunement.onRightClickBlock(stack, equipmentType, event)

            if (attunement.dirty) {
                SymphonyAPI.updateAttunement(stack, attunement)
            }
        }
    }

    @SubscribeEvent
    fun onUserDeath(event: LivingDeathEvent) {
        SymphonyAPI.runOnAllAttunements(event.entityLiving) { stack: ItemStack, equipmentType: EquipmentSlotType, attunement: Attunement ->
            attunement.onUserDeath(stack, equipmentType, event)

            if (attunement.dirty) {
                SymphonyAPI.updateAttunement(stack, attunement)
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onUserDrops(event: LivingDropsEvent) {
        val iterator = event.drops.iterator();

        while (iterator.hasNext()) {
            val next = iterator.next()
            val stack = next.item
            for (attunement in SymphonyAPI.getStackAttunements(stack)) {
                if (attunement.onUserDrops(stack, event)) {
                    iterator.remove()
                }

                if (attunement.dirty) {
                    SymphonyAPI.updateAttunement(stack, attunement)
                }
            }
        }
    }

    @SubscribeEvent
    fun onLivingUpdate(event: LivingEvent.LivingUpdateEvent) {
        val user = event.entityLiving
        SymphonyAPI.runOnAllAttunements(user) { stack: ItemStack, equipmentType: EquipmentSlotType, attunement: Attunement ->
            attunement.onUserUpdate(stack, equipmentType, user)

            if (attunement.dirty) {
                SymphonyAPI.updateAttunement(stack, attunement)
            }
        }
    }

    /**
     * This deals with attacks between two entities
     */
    @SubscribeEvent
    fun onAttackEntity(event: LivingDamageEvent) {
        val damageSource = event.source
        if (damageSource is ReflectedDamageSource) return

        val attacked = event.entityLiving
        val attacker = damageSource.trueSource ?: return

        val interaction = AttackInteraction(attacked, damageSource, event.amount)

        if (attacker is LivingEntity) {
            SymphonyAPI.runOnAllAttunements(attacker) { stack: ItemStack, equipmentType: EquipmentSlotType, attunement: Attunement ->
                attunement.onUserAttackEntity(stack, equipmentType, interaction)

                if (attunement.dirty) {
                    SymphonyAPI.updateAttunement(stack, attunement)
                }
            }
        }

        SymphonyAPI.runOnAllAttunements(attacked) { stack: ItemStack, equipmentType: EquipmentSlotType, attunement: Attunement ->
            attunement.onUserAttackedByEntity(stack, equipmentType, interaction)
        }

        val attackedPct = max(1.0f - interaction.reflection, 0.0f)
        val attackerPct = interaction.reflection

        if (attackerPct != 0f) {
            attacker.attackEntityFrom(ReflectedDamageSource(attacked), attackerPct * interaction.damage)

            if (attacker is LivingEntity) {
                interaction.effects.forEach {
                    attacker.addPotionEffect(EffectInstance(it.potion, (it.duration * attackerPct).toInt(), it.amplifier, it.isAmbient, it.doesShowParticles()))
                }
            }

            if (!attacker.isImmuneToFire) {
                val fire = ceil(interaction.fireTime * attackerPct).toInt()

                attacker.setFire(fire)
            }
        }

        event.amount = interaction.damage * attackedPct

        if (!attacked.isImmuneToFire) {
            val fire = ceil(interaction.fireTime * attackedPct).toInt()

            attacker.setFire(fire)
        }

        if (attackedPct != 0f) {
            interaction.effects.forEach { attacked.addPotionEffect(it) }
        }
    }
}