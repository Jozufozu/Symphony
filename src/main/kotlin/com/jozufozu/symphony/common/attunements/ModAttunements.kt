package com.jozufozu.symphony.common.attunements

import com.jozufozu.symphony.Symphony
import com.jozufozu.symphony.api.Attunement
import com.jozufozu.symphony.api.AttunementType
import net.alexwells.kottle.KotlinEventBusSubscriber
import net.minecraft.nbt.IntNBT
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.registries.ForgeRegistries

@KotlinEventBusSubscriber(bus = KotlinEventBusSubscriber.Bus.MOD)
object ModAttunements {
    private val attunements = arrayListOf<AttunementType<*>>()

    val buildUp = register("buildup", BasicAttunementType(IntNBT::class.java, ::BuildUpAttunement, ::BuildUpAttunement))
    val coalTorch = register("coal_torch", BasicAttunementType(IntNBT::class.java, ::LightCollectorAttunement, ::LightCollectorAttunement))
    val soulBound = register("soulbound", DatalessAttunementType(SoulBoundAttunement))

    @SubscribeEvent
    fun registerAttunements(event: RegistryEvent.Register<AttunementType<*>>) {
        val registry = event.registry
        for (enchantment in ForgeRegistries.ENCHANTMENTS) {
            registry.register(EnchantmentAttunementType(enchantment))
        }

        for (type in attunements) {
            registry.register(type)
        }
    }

    fun <A: Attunement, T: AttunementType<A>> register(name: String, type: T) = register(ResourceLocation(Symphony.MODID, name), type)

    fun <A: Attunement, T: AttunementType<A>> register(name: ResourceLocation, type: T): T {
        type.registryName = name
        attunements.add(type)
        return type
    }
}