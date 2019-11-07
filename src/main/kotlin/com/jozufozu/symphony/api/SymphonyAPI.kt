package com.jozufozu.symphony.api

import com.jozufozu.symphony.Symphony
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT
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
        return false
    }

    fun getStackAttunements(stack: ItemStack): List<Attunement> {
        val compound = stack.tag

        if (compound == null || compound.contains(registryName, Constants.NBT.TAG_COMPOUND)) return emptyList()

        val symphony = compound.getList(registryName, Constants.NBT.TAG_COMPOUND)

        val attunements = ArrayList<Attunement>()

        val numSockets = symphony.size

        for (i in 0 until numSockets) {
            val book = symphony.getCompound(i)

            for (name in book.keySet()) {
                val location = ResourceLocation(name)
                val enchantment = registry.getValue(location) ?: continue

                try {
                    attunements.add(enchantment.deserialize(book[name] ?: continue))
                } catch (e: Exception) {
                    log.error("Tried to initialize a note of type '$location', but it has no default constructor! This is a programmer error!")
                }

            }
        }

        return attunements
    }
}
