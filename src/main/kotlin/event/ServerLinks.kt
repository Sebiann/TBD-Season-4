package event

import Config
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLinksSendEvent


class ServerLinks(val config: Config) : Listener {
    private val mm = MiniMessage.miniMessage()

    @EventHandler
    @Suppress("UnstableApiUsage")
    fun onPlayerLinksSendEvent(event: PlayerLinksSendEvent) {
        config.links.sortedBy { it.order }.forEach {
            event.links.addLink(mm.deserialize(it.component), it.uri)
        }
    }
}
