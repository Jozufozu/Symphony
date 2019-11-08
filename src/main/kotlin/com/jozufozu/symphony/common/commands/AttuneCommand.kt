package com.jozufozu.symphony.common.commands

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.command.arguments.EntityArgument

object AttuneCommand {
    fun register(dispatcher: CommandDispatcher<CommandSource>) = dispatcher.register(Commands.literal("attune")
            .requires { it.hasPermissionLevel(2) }
            .then(Commands.argument("targets", EntityArgument.entities())))
}