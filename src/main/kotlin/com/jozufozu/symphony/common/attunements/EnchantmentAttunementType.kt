package com.jozufozu.symphony.common.attunements

import com.jozufozu.symphony.api.Attunement
import com.jozufozu.symphony.api.AttunementType
import net.minecraft.enchantment.Enchantment
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.nbt.IntNBT
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextFormatting

class EnchantmentAttunementType(val enchantment: Enchantment) : AttunementType<EnchantmentAttunementType.AttunementEnchantment>() {
    init {
        registryName = enchantment.registryName
    }

    override fun canBeApplied(stack: ItemStack): Boolean = enchantment.canApply(stack)

    override fun attune(stack: ItemStack, attunement: Attunement) {
        (attunement as? AttunementEnchantment)?.let {
            stack.addEnchantment(enchantment, it.level)
        }
    }

    override fun remove(stack: ItemStack, attuned: Attunement) {
        (attuned as? AttunementEnchantment)?.let { note ->
            val list = stack.enchantmentTagList.listIterator()

            for (tag in list) {
                if (tag is CompoundNBT) {
                    val name = ResourceLocation.tryCreate(tag.getString("id"))
                    if (enchantment.registryName == name && note.level == tag.getInt("lvl")) {
                        list.remove()
                    }
                }
            }
        }
    }

    override fun create() = AttunementEnchantment(0)

    override fun deserialize(nbt: INBT) = AttunementEnchantment((nbt as? IntNBT)?.int ?: throw IllegalArgumentException(""))

    inner class AttunementEnchantment(level: Int) : LeveledAttunement(0) {
        fun getEnchantment(): Enchantment = enchantment

        override fun getDisplay(advanced: Boolean): ITextComponent = with(enchantment.getDisplayName(level)) {
            val style = with(Style()) {
                setColor(TextFormatting.AQUA)
            }
            setStyle(style)
        }

        override val colorRGB: Int = 0
    }
}