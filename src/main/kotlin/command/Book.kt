package command

import chat.Formatting

import io.papermc.paper.command.brigadier.CommandSourceStack

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.BookMeta

import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Book {
    @Command("book author <name>")
    @Permission("tbd.command.book")
    fun setAuthor(css: CommandSourceStack, name: Array<String>) {
        if(css.sender is Player) {
            val player = css.sender as Player
            if(player.inventory.itemInMainHand.type == Material.WRITTEN_BOOK) {
                val book = player.inventory.itemInMainHand
                val bookMeta = book.itemMeta as BookMeta
                bookMeta.lore(listOf(
                    Formatting.allTags.deserialize("<dark_gray>✎<reset>")
                ))
                bookMeta.author(Formatting.restrictedTags.deserialize(name.joinToString(" ")))
                book.itemMeta = bookMeta
            }
        }
    }

    @Command("book title <title>")
    @Permission("tbd.command.book")
    fun setTitle(css: CommandSourceStack, title: Array<String>) {
        if(css.sender is Player) {
            val player = css.sender as Player
            if(player.inventory.itemInMainHand.type == Material.WRITTEN_BOOK) {
                val book = player.inventory.itemInMainHand
                val bookMeta = book.itemMeta as BookMeta
                bookMeta.lore(listOf(
                    Formatting.allTags.deserialize("<dark_gray>✎<reset>")
                ))
                bookMeta.title(Formatting.restrictedTags.deserialize(title.joinToString(" ")))
                book.itemMeta = bookMeta
            }
        }
    }
}