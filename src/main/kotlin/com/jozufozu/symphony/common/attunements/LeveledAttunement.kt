package com.jozufozu.symphony.common.attunements

import com.jozufozu.symphony.api.Attunement
import net.minecraft.nbt.IntNBT

abstract class LeveledAttunement(val level: Int) : Attunement() {
    constructor(nbt: IntNBT): this(nbt.int)

    override fun serializeNBT() = IntNBT(level)
}
