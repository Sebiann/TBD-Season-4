package event.util

import Config
import chat.Formatting.allTags

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLinksSendEvent

class ServerLinks(val config: Config) : Listener {
    @EventHandler
    @Suppress("UnstableApiUsage")
    fun onPlayerLinksSendEvent(event: PlayerLinksSendEvent) {
        config.links.sortedBy { it.order }.forEach {
            event.links.addLink(allTags.deserialize(it.component), it.uri)
        }
    }
}
