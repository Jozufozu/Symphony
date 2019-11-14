package com.jozufozu.symphony.common.attunements

import com.jozufozu.symphony.api.Attunement
import com.jozufozu.symphony.api.AttunementSerializationException
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

class EnchantmentAttunementType(val enchantment: Enchantment) : AttunementType<EnchantmentAttunementType.EnchantmentAttunement>() {
    init {
        registryName = enchantment.registryName
    }

    override fun create() = EnchantmentAttunement(0)

    override fun deserialize(nbt: INBT): EnchantmentAttunement {
        val level = (nbt as? IntNBT)?.int ?: throw AttunementSerializationException("")
        return EnchantmentAttunement(level)
    }

    inner class EnchantmentAttunement(val level: Int) : Attunement({ this }) {

        override fun canBeApplied(stack: ItemStack): Boolean = enchantment.canApply(stack)

        override fun attune(stack: ItemStack) {
            stack.addEnchantment(enchantment, level)
        }

        override fun remove(stack: ItemStack) {
            val list = stack.enchantmentTagList.listIterator()

            for (tag in list) {
                if (tag is CompoundNBT) {
                    val name = ResourceLocation.tryCreate(tag.getString("id"))
                    if (enchantment.registryName == name && level == tag.getInt("lvl")) {
                        list.remove()
                    }
                }
            }
        }

        fun getEnchantment(): Enchantment = enchantment

        override fun getDisplay(advanced: Boolean): ITextComponent = with(enchantment.getDisplayName(level)) {
            val style = with(Style()) {
                setColor(TextFormatting.DARK_GREEN)
            }
            setStyle(style)
        }

        override fun serializeNBT() = IntNBT(level)
    }
}