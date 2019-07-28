package com.jozufozu.quench.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class NoteType extends IForgeRegistryEntry.Impl<NoteType>
{
    /**
     * Whether or not this type of note can be applied to the given item stack.
     * {@link NoteType::attune} will not be called if this returns false.
     */
    public abstract boolean canBeApplied(ItemStack stack);

    /**
     * Do any <em>additional</em> modifications here, and return a {@link Attunement} that this type corresponds to.
     * <em>DO NOT SAVE THE NOTE TO THE ITEMSTACK HERE. THAT IS HANDLED BY SYMPHONIC ATTUNEMENT</em>
     */
    public void attune(ItemStack stack, Attunement attunement) {  }

    public void remove(ItemStack stack, Attunement attuned) {  }

    /**
     * The class of note that this type corresponds to.
     * <em>MUST HAVE A DEFAULT CONSTRUCTOR</em>
     */
    public abstract Attunement instantiate();

    // TODO: Add some way to get variations of this note type for loot generation
}
