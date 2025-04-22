package command

import chat.Formatting.allTags

import io.papermc.paper.command.brigadier.CommandSourceStack

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Flex {
    @Command("flex")
    @Permission("tbd.command.flex")
    fun echo(css: CommandSourceStack) {
        val player = css.sender as Player
        val itemStack = player.inventory.itemInMainHand
        if (itemStack.isEmpty) {
            player.sendMessage("You are not holding an item.\n\nIdiot...\n")
        } else {
            var stackSize = ""
            if (itemStack.amount > 1) {
                stackSize = "${itemStack.amount}x "
            }
            Bukkit.getServer().sendMessage(
                allTags.deserialize("${player.name} shows off $stackSize")
                    .append(itemStack.effectiveName().hoverEvent(itemStack))
            )
        }
    }
}
