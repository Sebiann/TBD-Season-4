package util

import org.bukkit.Material.AIR
import org.bukkit.entity.Player

/**
 * Check if the Player is holding something in their main hand. (!= AIR)
 */
fun Player.isHoldingItemInMainHand(): Boolean {
    return this.inventory.itemInMainHand.type != AIR
}
