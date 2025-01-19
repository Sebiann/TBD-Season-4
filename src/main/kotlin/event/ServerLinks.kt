package event

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLinksSendEvent
import java.net.URI


class ServerLinks : Listener {
    private val mm = MiniMessage.miniMessage()
    private val links: List<ServerLinkEntry> = listOf(
        ServerLinkEntry(
            mm.deserialize("<rainbow>made you look lol</rainbow>"),
            URI("https://links.tbdsmp.net/madeyoulook")
        ),
        ServerLinkEntry(
            mm.deserialize("<b><u><dark_red>BIGRAT DOT SOCIAL</dark_red></u></b>"),
            URI("https://bigrat.social")
        ),
    )

    @EventHandler
    @Suppress("UnstableApiUsage")
    fun onPlayerLinksSendEvent(event: PlayerLinksSendEvent) {
        links.forEach {
            event.links.addLink(it.component, it.uri)
        }
    }
}

data class ServerLinkEntry(val component: Component, val uri: URI)