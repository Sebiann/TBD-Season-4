package item

enum class ItemRarity(val rarityName : String, val rarityColour : String, val rarityGlyph : String, val rarityColourRGB: Triple<Int, Int, Int>) {
    COMMON("Common", "#ffffff", "\uF001", Triple(255, 255, 255)),
    UNCOMMON("Uncommon", "#0ed145", "\uF002", Triple(14, 209, 69)),
    RARE("Rare", "#00a8f3", "\uF003", Triple(0, 168, 243)),
    EPIC("Epic", "#b83dba", "\uF004", Triple(184, 61, 186)),
    LEGENDARY("Legendary", "#ff7f27", "\uF005", Triple(255, 127, 39)),
    MYTHIC("Mythic", "#ff3374", "\uF006", Triple(255, 51, 116)),
    SPECIAL("Special", "#ec1c24", "\uF007", Triple(236, 28, 36)),
    UNREAL("Unreal", "#8666e6", "\uF008", Triple(134, 102, 230))
}