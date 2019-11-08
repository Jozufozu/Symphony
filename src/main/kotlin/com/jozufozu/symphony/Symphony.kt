package com.jozufozu.symphony

import com.jozufozu.symphony.api.AttunementType
import com.jozufozu.symphony.api.SymphonyAPI
import net.alexwells.kottle.FMLKotlinModLoadingContext
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.RegistryBuilder

@Mod(Symphony.MODID)
object Symphony {
    const val MODID = "symphony"

    init {
        FMLKotlinModLoadingContext.get().modEventBus.addListener<RegistryEvent.NewRegistry> { onRegistryRegister(it) }
        FMLKotlinModLoadingContext.get().modEventBus.addGenericListener(AttunementType::class.java) { SymphonyAPI.log.info("REGISTER STUFF") }
    }


    fun onRegistryRegister(event: RegistryEvent.NewRegistry) {
        SymphonyAPI.registry = RegistryBuilder<AttunementType<*>>().setType(AttunementType::class.java).setName(SymphonyAPI.registryId).create()
    }
}
