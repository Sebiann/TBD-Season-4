package command

import chat.Formatting
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.entity.Player
import org.checkerframework.checker.units.qual.C
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import org.incendo.cloud.annotations.suggestion.Suggestions
import org.incendo.cloud.context.CommandContext

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Retexture {
    val textureOptions: List<String> = listOf("dunce_cap", "dunce_cap")

    @Command("retexture <option>")
    @CommandDescription("Retexture thingi.")
    @Permission("tbd.command.retexture")
    fun retexture(css: CommandSourceStack, @Argument(suggestions = "texture-option") option: Array<String>) {
        val player = css.sender as? Player ?: return
        if (option.isEmpty() || !textureOptions.contains(option[0])) {
            player.sendMessage(Formatting.allTags.deserialize("<red>Invalid or missing option. Valid options: ${textureOptions.joinToString(", ")}"))
            return
        }

        val command = String.format(
            "item modify entity %s armor.head {function:\"minecraft:set_components\", components: {item_model:\"tbdsmp:%s\", equippable:{slot: \"head\"}}}",
            player.name,
            option[0]
        )

        try {
            // Execute the Minecraft command
            player.server.dispatchCommand(player.server.consoleSender, command)
                player.sendMessage(Formatting.allTags.deserialize("<tbdcolour>Successfully applied reskin: ${option[0]}"))
        } catch (e: Exception) {
                player.sendMessage(Formatting.allTags.deserialize("<red>Error applying reskin: ${e.message}"))
        }
    }

    @Suggestions("texture-option")
    fun containerSuggestions(
        context: CommandContext<C?>?,
        input: String?
    ): List<String?> {
        return textureOptions
    }
}