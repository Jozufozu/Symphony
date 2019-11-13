package com.jozufozu.symphony.common.commands

import com.jozufozu.symphony.Symphony
import com.jozufozu.symphony.api.Attunement
import com.jozufozu.symphony.api.AttunementType
import com.jozufozu.symphony.api.SymphonyAPI
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.alexwells.kottle.KotlinEventBusSubscriber
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.command.ISuggestionProvider
import net.minecraft.command.arguments.ArgumentSerializer
import net.minecraft.command.arguments.ArgumentTypes
import net.minecraft.command.arguments.EntityArgument
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.nbt.JsonToNBT
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import java.util.concurrent.CompletableFuture

@KotlinEventBusSubscriber(bus = KotlinEventBusSubscriber.Bus.FORGE)
object AttuneCommand {
    init {
        ArgumentTypes.register("${Symphony.MODID}:attunement", AttunementArgument::class.java, ArgumentSerializer<AttunementArgument> { AttunementArgument() })
    }

    @SubscribeEvent
    fun registerCommand(event: FMLServerStartingEvent) {
        event.commandDispatcher.register(Commands.literal("attune")
                .requires { it.hasPermissionLevel(2) }
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("attunement", AttunementArgument())
                                .suggests { _, builder ->
                                    ISuggestionProvider.suggest(SymphonyAPI.registry.keys.stream().map {id -> id.toString() }, builder)
                                }.executes {
                                    attune(it, EntityArgument.getEntities(it, "targets"), AttunementArgument.getAttunemnet(it, "attunement"))
                                }
                        )
                )
        )
    }

    private fun attune(ctx: CommandContext<CommandSource>, targets: Collection<Entity>, input: AttunementInput): Int {
        val attunement = input.createAttunement()

        var count = 0
        for (entity in targets) {
            if (entity is LivingEntity) {
                if (SymphonyAPI.attuneStack(entity.heldItemMainhand, attunement))
                    count++
            }
        }

        return count
    }

    class AttunementArgument : ArgumentType<AttunementInput> {

        @Throws(CommandSyntaxException::class)
        override fun parse(reader: StringReader): AttunementInput {
            val parser = AttunementParser(reader).parse()
            return AttunementInput(parser.attunement, parser.nbt)
        }

        override fun <S> listSuggestions(p_listSuggestions_1_: CommandContext<S>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
            val reader = StringReader(builder.input)
            reader.cursor = builder.start
            val parser = AttunementParser(reader)

            try {
                parser.parse()
            } catch (var6: CommandSyntaxException) {
            }

            return parser.listSuggestions(builder)
        }

        override fun getExamples(): Collection<String> {
            return EXAMPLES
        }

        companion object {
            private val EXAMPLES = listOf("mending", "minecraft:mending", "symphony:third{2}")



            fun <S> getAttunemnet(ctx: CommandContext<S>, name: String): AttunementInput {
                return ctx.getArgument(name, AttunementInput::class.java) as AttunementInput
            }
        }
    }


    class AttunementInput(val attunementType: AttunementType<*>, private val tag: INBT?) {

        @Throws(CommandSyntaxException::class)
        fun createAttunement(): Attunement = if (tag == null ) attunementType.create() else attunementType.deserialize(tag)

        fun serialize(): String {
            val builder = StringBuilder(SymphonyAPI.registry.getKey(this.attunementType).toString())
            if (this.tag != null) {
                if (tag is CompoundNBT) builder.append(this.tag)
                else {
                    builder.append('{')
                    builder.append(this.tag)
                    builder.append('}')
                }
            }

            return builder.toString()
        }

        companion object {
            private val STACK_TOO_LARGE = Dynamic2CommandExceptionType { p_208695_0_, p_208695_1_ -> TranslationTextComponent("arguments.item.overstacked", *arrayOf(p_208695_0_, p_208695_1_)) }
        }
    }

    class AttunementParser(private val reader: StringReader) {
        lateinit var attunement: AttunementType<*>
            private set
        var nbt: INBT? = null
            private set
        private var suggestor: (SuggestionsBuilder) -> CompletableFuture<Suggestions> = { it.buildFuture() }

        @Throws(CommandSyntaxException::class)
        fun readItem() {
            val lastCursor = this.reader.cursor
            val registryName = ResourceLocation.read(this.reader)
            val value = SymphonyAPI.registry.getValue(registryName)
            if (value == null) {
                this.reader.cursor = lastCursor
                throw badAttunementId.createWithContext(this.reader, registryName.toString())
            }
            attunement = value
        }

        @Throws(CommandSyntaxException::class)
        fun parse(): AttunementParser {
            this.readItem()
            this.suggestor = { this.nbtSuggestor(it) }

            if (this.reader.canRead() && this.reader.peek() == '{') {
                this.suggestor = { it.buildFuture() }
                this.nbt = try {
                    JsonToNBT(this.reader).readStruct()
                } catch (_: Throwable) {
                    JsonToNBT(this.reader).readValue()
                }
            }

            return this
        }

        private fun nbtSuggestor(builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
            if (builder.remaining.isEmpty()) {
                builder.suggest('{'.toString())
            }

            return builder.buildFuture()
        }

        fun listSuggestions(builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
            return this.suggestor(builder.createOffset(this.reader.cursor))
        }

        companion object {
            val badAttunementId = DynamicCommandExceptionType { p_208696_0_ -> TranslationTextComponent("argument.item.id.invalid", p_208696_0_) }
        }
    }

}