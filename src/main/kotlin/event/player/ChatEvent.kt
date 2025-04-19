package event.player

import chat.GlobalRenderer

import io.papermc.paper.event.player.AsyncChatEvent

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ChatEvent: Listener {
    @EventHandler
    private fun onChat(e: AsyncChatEvent) {
        e.renderer(GlobalRenderer)
    }
}