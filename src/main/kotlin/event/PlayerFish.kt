package event

import fishing.Fishing

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
            Fishing.playerCaughtFish(event.player, item, (event.caught as Item).location, null, null)
        }
    }
}