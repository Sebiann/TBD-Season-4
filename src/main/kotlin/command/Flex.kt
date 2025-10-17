package command

import chat.Formatting

import io.papermc.paper.command.brigadier.CommandSourceStack

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Flex {
    @Command("flex")
    @Permission("tbd.command.flex")
    fun flex(css: CommandSourceStack) {
        val player = css.sender as Player
        Flexing.flex(player, null)
    }
}

object Flexing {
    fun flex(player: Player, itemToShare: ItemStack?) {
        val itemStack = itemToShare ?: player.inventory.itemInMainHand

        if (itemStack.isEmpty) {
            player.sendMessage(Formatting.allTags.deserialize("<red>You are not holding an item. Idiot...</red>"))
        } else {
            var stackSize = ""
            if (itemStack.amount > 1) {
                stackSize = "${itemStack.amount}x "
            }
            Bukkit.getServer().sendMessage(
                Formatting.allTags.deserialize("<tbdcolour>${player.name}</tbdcolour> shows off $stackSize")
                    .append(itemStack.effectiveName().hoverEvent(itemStack))
            )
        }
    }
}