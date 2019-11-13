package com.jozufozu.symphony.api

import net.minecraft.nbt.INBT
import net.minecraftforge.registries.ForgeRegistryEntry

abstract class AttunementType<T : Attunement> : ForgeRegistryEntry<AttunementType<*>>() {
    /**
     * Create a "zeroed" instance of this attunement
     */
    abstract fun create(): T

    @Throws(AttunementSerializationException::class)
    abstract fun deserialize(nbt: INBT): T

    // TODO: Add some way to get variations of this note type for loot generation
}
