package com.jozufozu.symphony.common.attunements

import com.jozufozu.symphony.api.Attunement
import com.jozufozu.symphony.api.AttunementSerializationException
import com.jozufozu.symphony.api.AttunementType
import net.minecraft.nbt.INBT

class BasicVariableAttunementType<T: Attunement, NBT: INBT>(
        val data: Class<NBT>,
        val factory: (AttunementType<*>) -> T,
        val deserializer: (AttunementType<*>, NBT) -> T
) : AttunementType<T>() {
    override fun create(): T = factory(this)

    override fun deserialize(nbt: INBT): T =
            if (data.isInstance(nbt)) deserializer(this, data.cast(nbt))
            else throw AttunementSerializationException("Expected instance of ${data.name}")
}