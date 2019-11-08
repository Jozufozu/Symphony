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
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.registries.ForgeRegistries

class EnchantmentAttunementType(val enchantment: Enchantment) : AttunementType<EnchantmentAttunementType.EnchantmentAttunement>() {
    init {
        registryName = enchantment.registryName
    }

    override fun canBeApplied(stack: ItemStack): Boolean = enchantment.canApply(stack)

    override fun attune(stack: ItemStack, attunement: Attunement) {
        (attunement as? EnchantmentAttunement)?.let {
            stack.addEnchantment(enchantment, it.level)
        }
    }

    override fun remove(stack: ItemStack, attuned: Attunement) {
        (attuned as? EnchantmentAttunement)?.let { note ->
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

    override fun create() = EnchantmentAttunement(0)

    override fun deserialize(nbt: INBT) = EnchantmentAttunement((nbt as? IntNBT)?.int ?: throw AttunementSerializationException(""))

    inner class EnchantmentAttunement(level: Int) : LeveledAttunement(level) {
        constructor(nbt: IntNBT): this(nbt.int)

        override val name: ResourceLocation get() = registryName!!

        fun getEnchantment(): Enchantment = enchantment

        override fun getDisplay(advanced: Boolean): ITextComponent = with(enchantment.getDisplayName(level)) {
            val style = with(Style()) {
                setColor(TextFormatting.AQUA)
            }
            setStyle(style)
        }

        override val colorRGB: Int = 0
    }

    companion object {
        fun registerAttunements(event: RegistryEvent.Register<AttunementType<*>>) {
            for (enchantment in ForgeRegistries.ENCHANTMENTS) {
                event.registry.register(EnchantmentAttunementType(enchantment))
            }
        }
    }
}