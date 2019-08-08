package com.jozufozu.symphony.common.attunements

import com.jozufozu.symphony.api.Attunement
import net.minecraft.nbt.INBT
import net.minecraft.nbt.IntNBT
import java.util.*

abstract class AttunementLeveled : Attunement<IntNBT>() {
    var level: Int = 0
        protected set

    override fun serializeNBT() = IntNBT(level)

    override fun deserializeNBT(nbt: IntNBT) {
        this.level = nbt.int
    }
}
