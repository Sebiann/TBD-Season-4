package event.player

import chat.Formatting
import lib.Sounds.ENDER_EYE_PLACE_FAIL
import lib.Sounds.ENDER_EYE_PLACE_FAIL_BACKGROUND
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType
import util.Keys.TRUE_EYE
import util.secondsToTicks

class EnderEyeInteract: Listener {
    @EventHandler
    fun enderEyeInteractEvent(event: PlayerInteractEvent) {
        if (!event.action.isRightClick) return
        if (!listOf(Material.END_PORTAL_FRAME).contains(event.clickedBlock?.type)) return
        if (event.item == null) return
        if (event.item!!.type != Material.ENDER_EYE) return
        if (event.item!!.type == Material.ENDER_EYE && event.player.hasCooldown(Material.ENDER_EYE)) return
        val item = event.item!!

        val isTrueEye = item.persistentDataContainer.get(TRUE_EYE, PersistentDataType.BOOLEAN) ?: false
        if (!isTrueEye) {
            event.isCancelled = true
            val player = event.player
            val block = event.clickedBlock!!
            if (player.getCooldown(item) != 0) return
            player.setCooldown(item, 10.secondsToTicks())
            player.sendActionBar(Formatting.allTags.deserialize("<red>These types of Eye of Ender don't seem to work in this universe..."))
            block.world.playSound(ENDER_EYE_PLACE_FAIL, block.location.x, block.location.y, block.location.z)
            block.world.playSound(ENDER_EYE_PLACE_FAIL_BACKGROUND, block.location.x, block.location.y, block.location.z)
            player.velocity = player.location.direction.normalize().multiply(-1).multiply(0.5)
        }
    }
}