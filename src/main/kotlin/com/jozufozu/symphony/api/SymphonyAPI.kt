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

    fun runOnAllAttunements(entity: LivingEntity, action: (ItemStack, EquipmentSlotType, Attunement) -> Unit) {
        for (equipmentSlotType in EquipmentSlotType.values()) {
            val stack = entity.getItemStackFromSlot(equipmentSlotType)
            for (attunement in getStackAttunements(stack)) {
                action(stack, equipmentSlotType, attunement)
            }
        }
    }

    fun getAllAttunements(entity: LivingEntity): List<Attunement> {
        return Arrays.stream(EquipmentSlotType.values())
                .map(entity::getItemStackFromSlot)
                .flatMap { stack -> getStackAttunements(stack).stream() }
                .collect(Collectors.toList())
    }

    fun attuneStack(stack: ItemStack, attunement: Attunement): Boolean {
        if (attunement.canBeApplied(stack)) {
            val symphony = stack.orCreateTag.getCompound(registryName)

            val name = attunement.type.registryName?.toString() ?: return false
            if (symphony.contains(name)) {
                return false
            }
            symphony.put(name, attunement.serializeNBT())
            attunement.attune(stack)
            stack.setTagInfo(registryName, symphony)
            return true
        }
        return false
    }

    fun updateAttunement(stack: ItemStack, attunement: Attunement) {
        val symphony = stack.orCreateTag.getCompound(registryName)

        val name = attunement.type.registryName?.toString() ?: return
        symphony.put(name, attunement.serializeNBT())
        attunement.remove(stack)
        attunement.attune(stack)
        stack.setTagInfo(registryName, symphony)
    }

    fun getStackAttunements(stack: ItemStack): List<Attunement> {
        val compound = stack.tag

        if (compound == null || !compound.contains(registryName, Constants.NBT.TAG_COMPOUND)) return emptyList()

        val symphony = compound.getCompound(registryName)

        val attunements = ArrayList<Attunement>()

        for (name in symphony.keySet()) {
            val nbt = symphony.get(name) ?: continue

            val location = ResourceLocation(name)
            val type = registry.getValue(location) ?: continue

            attunements.add(type.deserialize(nbt))
        }

        return attunements
    }
}
