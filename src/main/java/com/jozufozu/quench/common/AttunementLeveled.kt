package com.jozufozu.quench.common

import com.jozufozu.quench.api.Attunement
import net.minecraft.nbt.NBTTagInt

abstract class AttunementLeveled : Attunement<NBTTagInt>() {
    var level: Int = 0
        private set

    override fun serializeNBT() = NBTTagInt(level)

    override fun deserializeNBT(nbt: NBTTagInt) {
        this.level = nbt.int
    }
}
