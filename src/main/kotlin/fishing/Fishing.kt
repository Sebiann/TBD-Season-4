package fishing

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Item
import org.bukkit.entity.Player

object Fishing {

    fun playerCaughtFish(player: Player, item: Item) {
        val fishRarity = FishRarity.getRandomRarity()

        player.sendMessage(Component.text("You caught a $fishRarity fish."))
        val fishMeta = item.itemStack.itemMeta
        fishMeta.displayName(
            Component.text(item.name).color(TextColor.fromHexString(fishRarity.itemRarity.rarityColour)).decoration(TextDecoration.ITALIC, false)
        )
        fishMeta.lore(
            listOf(
                Component.text(fishRarity.itemRarity.rarityGlyph).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
            )
        )
        item.itemStack.setItemMeta(fishMeta)
    }

}