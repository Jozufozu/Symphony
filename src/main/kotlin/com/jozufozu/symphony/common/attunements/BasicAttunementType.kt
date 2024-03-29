package com.jozufozu.symphony.common.attunements

import com.jozufozu.symphony.api.Attunement
import com.jozufozu.symphony.api.AttunementSerializationException
import com.jozufozu.symphony.api.AttunementType
import net.minecraft.nbt.INBT

class BasicAttunementType<T: Attunement, NBT: INBT>(
        val data: Class<NBT>,
        val factory: () -> T,
        val deserializer: (NBT) -> T
) : AttunementType<T>() {
    override fun create(): T = factory()

    override fun deserialize(nbt: INBT): T =
            if (data.isInstance(nbt)) deserializer(data.cast(nbt))
            else throw AttunementSerializationException("Expected instance of ${data.name}")
}