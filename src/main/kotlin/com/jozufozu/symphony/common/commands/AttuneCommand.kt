package com.jozufozu.symphony.common.commands

import com.jozufozu.symphony.api.SymphonyAPI
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.command.arguments.EntityArgument
import net.minecraft.command.arguments.NBTTagArgument
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.INBT
import net.minecraft.util.ResourceLocation

object AttuneCommand {
    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        dispatcher.register(Commands.literal("attune")
                .requires { it.hasPermissionLevel(2) }
                .then(Commands.argument("targets", EntityArgument.entities()))
                .then(Commands.argument("attunement", StringArgumentType.string())).executes {
                    attune(it, EntityArgument.getEntities(it, "targets"), StringArgumentType.getString(it, "attunement"), null)
                }
                .then(Commands.argument("data", NBTTagArgument.func_218085_a())).executes {
                    attune(it, EntityArgument.getEntities(it, "targets"), StringArgumentType.getString(it, "attunement"), NBTTagArgument.func_218086_a(it, "data"))
                })
    }

    private fun attune(it: CommandContext<CommandSource>, targets: Collection<Entity>, name: String, data: INBT?): Int {
        val type = SymphonyAPI.registry.getValue(ResourceLocation(name)) ?: return 0

        val attunement = data?.let { type.deserialize(it) } ?: type.create()

        var count = 0
        for (entity in targets) {
            if (entity is LivingEntity) {
                if (SymphonyAPI.attuneStack(entity.heldItemMainhand, attunement))
                    count++
            }
        }

        return count
    }
}