package com.jozufozu.symphony.api

import net.minecraft.entity.LivingEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.util.Constants
import net.minecraftforge.registries.IForgeRegistry
import org.apache.logging.log4j.Logger
import java.util.*
import java.util.stream.Collectors

object SymphonyAPI {
    val NBT_ROOT = "symphony:attunements"

    lateinit var log: Logger

    lateinit var REGISTRY: IForgeRegistry<AttunementType<*>>

    fun getAllAttunements(entity: LivingEntity): List<Attunement> {
        return Arrays.stream(EquipmentSlotType.values())
                .map(entity::getItemStackFromSlot)
                .flatMap { stack -> getStackAttunements(stack).stream() }
                .collect(Collectors.toList())
    }

    fun isTargetableItem(stack: ItemStack): Boolean {
        return true
    }

    fun attuneStack(stack: ItemStack, attunement: Attunement): Boolean {
        return false
    }

    fun getStackAttunements(stack: ItemStack): List<Attunement> {
        val compound = stack.tag

        if (compound == null || compound.contains(NBT_ROOT, Constants.NBT.TAG_COMPOUND)) return emptyList()

        val quench = compound.getList(NBT_ROOT, Constants.NBT.TAG_COMPOUND)

        val attunements = ArrayList<Attunement>()

        val numSockets = quench.size

        for (i in 0 until numSockets) {
            val socket = quench.getCompound(i)

            for (name in socket.keySet()) {
                val location = ResourceLocation(name)
                val enchantment = REGISTRY.getValue(location) ?: continue

                try {
                    attunements.add(enchantment.deserialize(socket[name] ?: continue))
                } catch (e: Exception) {
                    log.error("Tried to initialize a note of type '$location', but it has no default constructor! This is a programmer error!")
                }

            }
        }

        return attunements
    }
}
