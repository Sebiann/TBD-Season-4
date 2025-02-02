package command

import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Flex {
    private val mm = MiniMessage.miniMessage()

    @Command("flex")
    @Permission("tbdseason4.command.flex")
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
                mm.deserialize("${player.name} shows off $stackSize")
                    .append(itemStack.effectiveName().hoverEvent(itemStack))
            )
        }
    }
}
