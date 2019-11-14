package com.jozufozu.symphony.common.attunements

import com.jozufozu.symphony.api.Attunement
import com.jozufozu.symphony.api.SymphonyAPI
import net.alexwells.kottle.KotlinEventBusSubscriber
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.ByteNBT
import net.minecraft.nbt.INBT
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.GameRules
import net.minecraftforge.common.util.FakePlayer
import net.minecraftforge.event.entity.living.LivingDropsEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent


@KotlinEventBusSubscriber(bus = KotlinEventBusSubscriber.Bus.FORGE)
object SoulBoundAttunement: Attunement({ ModAttunements.soulBound }) {
    override fun serializeNBT(): INBT = ByteNBT(0)

    override fun getDisplay(advanced: Boolean) = StringTextComponent("Soulbound").setStyle(Style().setColor(TextFormatting.GOLD))

    override fun canBeApplied(stack: ItemStack) = true

    override fun onUserDrops(stack: ItemStack, event: LivingDropsEvent): Boolean {
        val user = event.entityLiving
        if (user !is PlayerEntity) return false
        if (user is FakePlayer) return false
        if (user.world.gameRules.getBoolean(GameRules.KEEP_INVENTORY)) return false

        addToPlayerInventory(user, stack)

        return true
    }

    @SubscribeEvent
    fun onPlayerClone(event: PlayerEvent.Clone) {
        val oldPlayer = event.original
        val player = event.player
        if (!event.isWasDeath) return
        if (player is FakePlayer) return
        if (player.world.gameRules.getBoolean(GameRules.KEEP_INVENTORY)) return
        for (i in 0 until oldPlayer.inventory.armorInventory.size) {
            val stack = oldPlayer.inventory.armorInventory[i]

            if (SymphonyAPI.stackHasAttunement(stack, ModAttunements.soulBound) && addToPlayerInventory(player, stack)) {
                oldPlayer.inventory.armorInventory[i] = ItemStack.EMPTY
            }
        }
        for (i in 0 until oldPlayer.inventory.mainInventory.size) {
            val stack = oldPlayer.inventory.mainInventory[i]

            if (SymphonyAPI.stackHasAttunement(stack, ModAttunements.soulBound) && addToPlayerInventory(player, stack)) {
                oldPlayer.inventory.mainInventory[i] = ItemStack.EMPTY
            }
        }
    }

    fun addToPlayerInventory(player: PlayerEntity, stack: ItemStack): Boolean {
        if (stack.isEmpty) return false

        if (stack.item is ArmorItem) {
            val arm = stack.item as ArmorItem
            val index = arm.equipmentSlot.slotIndex
            if (player.inventory.armorInventory[index].isEmpty) {
                player.inventory.armorInventory[index] = stack
                return true
            }
        }
        val inv = player.inventory
        for (i in 0 until inv.mainInventory.size) {
            if (inv.mainInventory[i].isEmpty) {
                inv.mainInventory[i] = stack.copy()
                return true
            }
        }
        return false
    }
}