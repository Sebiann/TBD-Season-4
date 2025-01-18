package event

import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLinksSendEvent
import java.net.URI


class ServerLinks : Listener {
    @EventHandler
    @Suppress("UnstableApiUsage")
    fun onPlayerLinksSendEvent(event: PlayerLinksSendEvent) {
        event.links.addLink(Component.text("made you look"), URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"))
    }
}
