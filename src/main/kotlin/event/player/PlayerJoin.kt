package event.player

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoin : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        // TODO: proper RP setup
        event.player.setResourcePack("https://www.dropbox.com/scl/fi/l6j1o86uj5vjhg6x7sadw/TBD-SMP-Season-4.zip?rlkey=8f0btscuwjuubdmthm9i6j5yr&st=piglwajs&dl=1", "50a85d6b895b5f152ecae64b2cfbfbfe965152a3")
    }

}