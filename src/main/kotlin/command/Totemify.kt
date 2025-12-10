package command

import chat.Formatting
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.datacomponent.DataComponentTypes
import util.Sounds.RENAME_ITEM
import org.bukkit.GameMode

import org.bukkit.Material.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import util.isHoldingItemInMainHand

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Totemify {

    @Command("totemify")
    @CommandDescription("Turns the held item into a Totem at the cost of 1 Totem.")
    @Permission("tbd.command.totemify")
    fun totemifyItem(css: CommandSourceStack) {
        val player = css.sender as? Player ?: return

        if (!player.isHoldingItemInMainHand()) {
            player.sendMessage(Formatting.allTags.deserialize("<red>You need to be holding an item to totemify."))
            return
        }
        if (player.inventory.itemInMainHand.type == TOTEM_OF_UNDYING) {
            player.sendMessage(Formatting.allTags.deserialize("<red>You can't totemify a Totem of Undying."))
            return
        }

        if (!hasTotemInInventory(player) && player.gameMode !== GameMode.CREATIVE) {
            player.sendMessage(Formatting.allTags.deserialize("<red>You need 1 Totem of Undying in your inventory to totemify."))
            return
        }

        player.inventory.removeItem(ItemStack.of(TOTEM_OF_UNDYING))
        val deathProtection = io.papermc.paper.datacomponent.item.DeathProtection.deathProtection()
            .build()
        player.inventory.itemInMainHand.setData(DataComponentTypes.DEATH_PROTECTION, deathProtection)
        player.sendMessage(Formatting.allTags.deserialize("<tbdcolour>Totemified item!"))
        player.playSound(RENAME_ITEM)
    }

    fun hasTotemInInventory(player: Player): Boolean {
        return player.inventory.contains(TOTEM_OF_UNDYING, 1)
    }
}