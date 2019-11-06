package com.jozufozu.symphony.common.attunements

import com.jozufozu.symphony.api.Attunement
import com.jozufozu.symphony.api.AttunementType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.INBT

class PotionAttunementType : AttunementType<PotionAttunementType.PotionAttunement> {
    override fun canBeApplied(stack: ItemStack) = true

    override fun create(): Attunement {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deserialize(nbt: INBT): Attunement {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class PotionAttunement() : LeveledAttunement() {

    }
}