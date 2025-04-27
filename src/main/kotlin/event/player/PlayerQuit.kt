package event.player

import chat.Formatting

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

import util.Noxesium

class PlayerQuit: Listener {
    @EventHandler
    private fun onQuit(e: PlayerQuitEvent) {
        Noxesium.removeNoxesiumUser(e.player)
        e.quitMessage(Formatting.allTags.deserialize("<dark_gray>[<red>-<dark_gray>] <tbdcolour>${e.player.name}<reset> left the game."))
    }
}