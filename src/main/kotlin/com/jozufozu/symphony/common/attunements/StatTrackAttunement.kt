package com.jozufozu.symphony.common.attunements

import com.jozufozu.symphony.api.Attunement
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.nbt.INBT
import net.minecraft.util.text.ITextComponent

class StatTrackAttunement: Attunement {
    override fun getDisplay(tooltip: MutableList<ITextComponent>, advanced: ITooltipFlag, expand: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun canBeApplied(stack: ItemStack): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun serializeNBT(): INBT {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}