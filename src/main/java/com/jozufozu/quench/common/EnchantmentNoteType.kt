package com.jozufozu.quench.common

import com.jozufozu.quench.api.NoteType
import com.jozufozu.quench.api.Attunement
import net.minecraft.enchantment.Enchantment
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTBase
import net.minecraft.util.text.*
import java.util.*

class EnchantmentNoteType(val enchantment: Enchantment) : NoteType() {
    init {
        registryName = enchantment.registryName
    }

    /**
     * Whether or not this type of note can be applied to the given item stack.
     * [::attune][NoteType] will not be called if this returns false.
     */
    override fun canBeApplied(stack: ItemStack): Boolean = enchantment.canApply(stack)

    override fun attune(stack: ItemStack, attunement: Attunement<NBTBase>) {
        (attunement as? AttunementEnchantment)?.let {
            stack.addEnchantment(enchantment, it.level)
        }
    }

    override fun remove(stack: ItemStack, attuned: Attunement<NBTBase>) {
        (attuned as? AttunementEnchantment)?.let { note ->
            val list = stack.enchantmentTagList

            for (i in 0..list.tagCount()) list.getCompoundTagAt(i).let {
                if (Enchantment.getEnchantmentID(enchantment) == it.getShort("id").toInt() && note.level == it.getShort("lvl").toInt()) {
                    list.removeTag(i)
                }
            }
        }
    }

    /**
     * The class of note that this type corresponds to.
     * *MUST HAVE A DEFAULT CONSTRUCTOR*
     */
    override fun instantiate() = AttunementEnchantment()

    inner class AttunementEnchantment : AttunementLeveled() {
        fun getEnchantment(): Enchantment = enchantment

        override fun getDisplay(advanced: Boolean): ITextComponent? = with(TextComponentString(enchantment.getTranslatedName(level))) {
            val style = with(Style()) {
                setColor(TextFormatting.AQUA)
            }
            setStyle(style)
        }

        override fun getColorRGB(): Int = 0
    }
}