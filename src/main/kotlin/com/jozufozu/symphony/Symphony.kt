package com.jozufozu.symphony

import com.jozufozu.symphony.api.AttunementType
import com.jozufozu.symphony.api.SymphonyAPI
import com.jozufozu.symphony.common.attunements.EnchantmentAttunementType
import com.jozufozu.symphony.common.commands.AttuneCommand
import net.alexwells.kottle.FMLKotlinModLoadingContext
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import net.minecraftforge.registries.RegistryBuilder

@Mod(Symphony.MODID)
object Symphony {
    const val MODID = "symphony"

    init {
        FMLKotlinModLoadingContext.get().modEventBus.addListener(this::registerCommand)
        FMLKotlinModLoadingContext.get().modEventBus.addListener<RegistryEvent.NewRegistry> { onRegistryRegister(it) }
        FMLKotlinModLoadingContext.get().modEventBus.addGenericListener<RegistryEvent.Register<AttunementType<*>>, AttunementType<*>>(AttunementType::class.java) { EnchantmentAttunementType.registerAttunements(it) }
    }


    fun onRegistryRegister(event: RegistryEvent.NewRegistry) {
        SymphonyAPI.registry = RegistryBuilder<AttunementType<*>>().setType(AttunementType::class.java).setName(SymphonyAPI.registryId).create()
    }

    fun registerCommand(event: FMLServerStartingEvent) {
        AttuneCommand.register(event.commandDispatcher)
    }
}
