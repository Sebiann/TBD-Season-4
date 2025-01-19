package event

import fishing.FishRarity
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.FurnaceSmeltEvent
import org.bukkit.persistence.PersistentDataType
import util.Keys.FISH_RARITY

class FurnaceSmelt : Listener {

    @EventHandler
    fun onFurnaceSmelt(event: FurnaceSmeltEvent) {
        val fishRarityStr = event.source.persistentDataContainer.get(FISH_RARITY, PersistentDataType.STRING)

        if (fishRarityStr != null) {
            val fishRarity = FishRarity.valueOf(fishRarityStr)
            val resultMeta = event.result.itemMeta
            resultMeta.displayName(
                event.result.effectiveName().color(TextColor.fromHexString(fishRarity.itemRarity.rarityColour)).decoration(TextDecoration.ITALIC, false)
            )
            resultMeta.lore(event.source.lore())
            resultMeta.setEnchantmentGlintOverride(event.source.itemMeta.enchantmentGlintOverride)
            event.result.setItemMeta(resultMeta)
        }
    }
}
