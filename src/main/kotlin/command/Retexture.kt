package command

import Config
import TextureOptions
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
    @Command("retexture <option>")
    @CommandDescription("Retexture thingi.")
    @Permission("tbd.command.retexture")
    fun retexture(css: CommandSourceStack, @Argument(suggestions = "texture-option") option: Array<String>) {
        val player = css.sender as? Player ?: return

        val command = java.lang.String.format(
            "item modify entity %s armor.head {function:\"minecraft:set_components\", components: {item_model:\"tbdsmp:%s\", equippable:{slot: \"head\"}}}",
            player.name,
            option
        )
    }

    @Suggestions("texture-option")
    fun containerSuggestions(
        context: CommandContext<C?>?,
        input: String?
    ): List<TextureOptions?> {
        return List<TextureOptions>
    }
}