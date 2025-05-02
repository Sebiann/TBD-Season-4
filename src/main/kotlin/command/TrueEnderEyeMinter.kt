package command

import chat.Formatting.allTags
import fishing.Fishing
import io.papermc.paper.command.brigadier.CommandSourceStack
import item.ItemRarity
import item.ItemType
import lib.Sounds
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import org.incendo.cloud.annotation.specifier.Greedy
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import java.time.Duration

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class TrueEnderEyeMinter {
    @Command("trueeye <position> [lore]")
    @Permission("tbd.command.eye")
    fun mintEye(css: CommandSourceStack, position: Location, @Greedy lore: String?) {
        if(css.sender is Player) {
            val player = css.sender as Player
            val trueEye = ItemStack(Material.ENDER_EYE)
            val trueEyeMeta = trueEye.itemMeta
            position.world = player.world
            trueEyeMeta.displayName(
                allTags.deserialize("<${ItemRarity.MYTHIC.colourHex}>True Eye of Ender").decoration(TextDecoration.ITALIC, false)
            )
            val baseLore = mutableListOf<Component>(allTags.deserialize("<reset><white>${ItemRarity.MYTHIC.rarityGlyph}${ItemType.CONSUMABLE.typeGlyph}").decoration(TextDecoration.ITALIC, false), allTags.deserialize("<reset><yellow>One of the ancient eyes.").decoration(TextDecoration.ITALIC, false))
            if(lore != null) {
                baseLore.add(allTags.deserialize(lore))
            }
            trueEyeMeta.lore(baseLore)
            trueEyeMeta.setEnchantmentGlintOverride(true)
            trueEye.itemMeta = trueEyeMeta

            val trueEyeEntity = position.world.spawn(position, Item::class.java)
            trueEyeEntity.itemStack = trueEye
            trueEyeEntity.setGravity(false)
            trueEyeEntity.isGlowing = true
            trueEyeEntity.velocity = Vector().zero()

            trueEyeEntity.location.world.strikeLightningEffect(position)
            Fishing.firework(position, flicker = false, trail = false, ItemRarity.MYTHIC.colour, FireworkEffect.Type.BALL_LARGE, false)

            Bukkit.getServer().showTitle(
                Title.title(
                    allTags.deserialize("<${ItemRarity.MYTHIC.colourHex}><b>TRUE EYE OF ENDER<reset>"),
                    allTags.deserialize("A true eye of ender has spawned..."),
                    Title.Times.times(Duration.ofMillis(250L), Duration.ofSeconds(5L), Duration.ofMillis(250L))
                )
            )
            Bukkit.getServer().playSound(Sounds.TRUE_EYE_SPAWN)
            //player.inventory.addItem(trueEye)
        }
    }
}