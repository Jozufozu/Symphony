package com.jozufozu.symphony.common.attunements

import com.jozufozu.symphony.api.Attunement
import com.jozufozu.symphony.api.AttunementType
import net.minecraft.nbt.INBT

class DatalessAttunementType<T: Attunement>(private val instance: T): AttunementType<T>() {
    override fun create() = instance

    override fun deserialize(nbt: INBT) = instance
}