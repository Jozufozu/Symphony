package com.jozufozu.symphony.api

import net.minecraft.item.ItemStack
import net.minecraft.nbt.INBT
import net.minecraftforge.registries.ForgeRegistryEntry

abstract class AttunementType<T : Attunement> : ForgeRegistryEntry<AttunementType<*>>() {
    /**
     * Whether or not this type of note can be applied to the given item stack.
     * [::attune][AttunementType] will not be called if this returns false.
     */
    abstract fun canBeApplied(stack: ItemStack): Boolean

    /**
     * Do any *additional* modifications here, and return a [Attunement] that this type corresponds to.
     * *DO NOT SAVE THE NOTE TO THE ITEMSTACK HERE. THAT IS HANDLED BY SYMPHONIC ATTUNEMENT*
     */
    open fun attune(stack: ItemStack, attunement: Attunement) {}

    open fun remove(stack: ItemStack, attuned: Attunement) {}

    /**
     * Create a "zeroed" instance of this attunement
     */
    abstract fun create(): Attunement

    abstract fun deserialize(nbt: INBT): Attunement

    // TODO: Add some way to get variations of this note type for loot generation
}
