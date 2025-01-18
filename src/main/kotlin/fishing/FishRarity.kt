package fishing

import item.ItemRarity
import logger
import kotlin.random.Random

enum class FishRarity(val weight: Double, val itemRarity: ItemRarity) {
    COMMON(49.75, ItemRarity.COMMON),
    UNCOMMON(30.0, ItemRarity.UNCOMMON),
    RARE(15.0, ItemRarity.RARE),
    EPIC(17.0, ItemRarity.EPIC),
    LEGENDARY(1.0, ItemRarity.LEGENDARY),
    MYTHIC(0.2, ItemRarity.MYTHIC),
    UNREAL(0.05, ItemRarity.UNREAL),
    SPECIAL(0.0, ItemRarity.SPECIAL);


    companion object {
        fun getRandomRarity(): FishRarity {
            val totalWeight = entries.sumOf { it.weight }
            val randomValue = Random.nextDouble(totalWeight)

            var cumulativeWeight = 0.0
            for (rarity in entries) {
                cumulativeWeight += rarity.weight
                if (randomValue < cumulativeWeight) {
                    return rarity
                }
            }

            logger.warning("Unreachable code hit! No rarity selected")
            return SPECIAL // Should be unreachable but default to small in case of issue
        }
    }
}
