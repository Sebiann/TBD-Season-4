package event.player

import chat.Formatting
import logger

import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.TranslationArgument
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

import kotlin.random.Random

class DeathEvent: Listener {
    @EventHandler
    private fun onDeath(e: PlayerDeathEvent) {
        if(e.deathMessage() !is TranslatableComponent) {
            logger.info("Another plugin edited this death message.")
            return
        }
        val component = e.deathMessage()!! as TranslatableComponent
        val args = component.arguments()
        val newArgs = mutableListOf<TranslationArgument>()
        args.forEach {
            val str = PlainTextComponentSerializer.plainText().serialize(it.asComponent())
            if ((str == e.player.name && e.player.isInvisible) || (str == e.player.killer?.name && e.player.killer?.isInvisible == true)) {
                newArgs.add(TranslationArgument.component(Formatting.allTags.deserialize("<obfuscated>*</obfuscated>".repeat(Random.nextInt(4, 16)))))
            } else {
                newArgs.add(it)
            }
        }
        component.arguments(newArgs)
        e.deathMessage(component)
    }
}