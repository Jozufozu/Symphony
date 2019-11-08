package com.jozufozu.symphony.api

import com.jozufozu.symphony.Symphony
import net.minecraft.entity.LivingEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.util.Constants
import net.minecraftforge.registries.IForgeRegistry
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.*
import java.util.stream.Collectors

object SymphonyAPI {
    var log: Logger = LogManager.getLogger(Symphony.MODID)

    const val registryName = "${Symphony.MODID}:attunements"

    val registryId = ResourceLocation(registryName)

    lateinit var registry: IForgeRegistry<AttunementType<*>>

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
        registry.getValue(attunement.name)?.let {
            if (it.canBeApplied(stack)) {
                val symphony = stack.orCreateTag.getCompound(registryName)

                val name = attunement.name.toString()
                if (symphony.contains(name)) {
                    return false
                }
                symphony.put(name, attunement.serializeNBT())
                it.attune(stack, attunement)
                return true
            }
        }
        return false
    }

    fun getStackAttunements(stack: ItemStack): List<Attunement> {
        val compound = stack.tag

        if (compound == null || compound.contains(registryName, Constants.NBT.TAG_COMPOUND)) return emptyList()

        val symphony = compound.getCompound(registryName)

        val attunements = ArrayList<Attunement>()

        for (name in symphony.keySet()) {
            val attunementNBT = symphony.get(name) ?: continue

            val location = ResourceLocation(name)
            val enchantment = registry.getValue(location) ?: continue

            try {
                attunements.add(enchantment.deserialize(attunementNBT))
            } catch (exc: AttunementSerializationException) {
                // TODO
            }
        }

        return attunements
    }
}
