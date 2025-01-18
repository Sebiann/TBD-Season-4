package event

import fishing.Fishing
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Tag
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent

class PlayerFish : Listener {

    @EventHandler
    fun onPlayerFish(event: PlayerFishEvent) {
        if (event.state !== PlayerFishEvent.State.CAUGHT_FISH) return
        val item = event.caught as Item

        if (Tag.ITEMS_FISHES.isTagged(item.itemStack.type)) {
            Bukkit.broadcast(Component.text("FISH!!!!! ${item.name}"))
            Fishing.playerCaughtFish(event.player, item)
        } else {
            Bukkit.broadcast(Component.text("NOT FISH!!!!! ${item.name}"))
        }
    }
}