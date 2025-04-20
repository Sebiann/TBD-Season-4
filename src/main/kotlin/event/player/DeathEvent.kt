package event.player

import chat.Formatting
import logger
import net.kyori.adventure.text.Component

import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.TranslationArgument
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

import kotlin.random.Random

class DeathEvent: Listener {
    @EventHandler
    private fun onDeath(e: PlayerDeathEvent) {
        if(e.deathMessage() !is TranslatableComponent || e.deathMessage() == null ) {
            logger.info("Another plugin edited this death message.")
            return
        }
        if (e.player.isInvisible || e.player.killer?.isInvisible == true)
            logger.info("Original deathmesage: \"${plainText().serialize(e.deathMessage()!!)}\"")

        val component = e.deathMessage()!! as TranslatableComponent
        val newArgs = mutableListOf<TranslationArgument>()
        component.arguments().forEach {
            val str = plainText().serialize(it.asComponent())
            if ((str == e.player.name && e.player.isInvisible) || (str == e.player.killer?.name && e.player.killer?.isInvisible == true)) {
                newArgs.add(TranslationArgument.component(
                    Formatting.allTags.deserialize(
                        "<hover:show_text:'made you look'><obfuscated>${"*".repeat(Random.nextInt(4, 16))}</obfuscated></hover>"
                    )
                ))
            } else {
                newArgs.add(it)
            }
        }

        val newComponent = Component.translatable()
            .key(component.key())
            .arguments(newArgs)
            .build()
        e.deathMessage(newComponent)
    }
}