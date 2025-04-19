package chat

import chat.Formatting.allTags
import chat.Formatting.restrictedTags
import io.papermc.paper.chat.ChatRenderer

import lib.Sounds

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import util.Noxesium

object ChatUtility {
    /** Sends a message to the specified audience. **/
    fun messageAudience(recipient: Audience, message: String, restricted: Boolean, vararg placeholders: TagResolver) {
        val resolvers = mutableListOf<TagResolver>()
        for(p in placeholders) {
            resolvers.add(p)
        }

        recipient.sendMessage(formatMessage(message, restricted, TagResolver.resolver(resolvers)))
    }

    /** Formats a message, which can produce different results depending on if restricted or not. **/
    fun formatMessage(message: String, restricted: Boolean, vararg placeholders: TagResolver): Component {
        val resolvers = mutableListOf<TagResolver>()
        for(p in placeholders) {
            resolvers.add(p)
        }

        return if (restricted) {
            restrictedTags.deserialize(message, TagResolver.resolver(resolvers))
        } else {
            allTags.deserialize(message, TagResolver.resolver(resolvers))
        }
    }

    /** Sends a message to the admin channel which includes all online admins. **/
    fun broadcastAdmin(rawMessage: String, isSilent: Boolean) {
        val admin = Audience.audience(Bukkit.getOnlinePlayers()).filterAudience { (it as Player).hasPermission("tbd.group.admin") }
        admin.sendMessage(allTags.deserialize("<gray>[<reset><prefix:admin><gray>]<reset> $rawMessage"))
        if(!isSilent) {
            admin.playSound(Sounds.ADMIN_MESSAGE)
        }
    }

    /** Sends a message to the dev channel which includes all online devs. **/
    fun broadcastDev(rawMessage: String, isSilent: Boolean) {
        val dev = Audience.audience(Bukkit.getOnlinePlayers()).filterAudience { (it as Player).hasPermission("tbd.group.dev") }
        dev.sendMessage(allTags.deserialize("<gray>[<reset><prefix:dev><gray>]<reset> $rawMessage"))
        if(!isSilent) {
            dev.playSound(Sounds.ADMIN_MESSAGE)
        }
    }
}

object GlobalRenderer : ChatRenderer {
    override fun render(source: Player, sourceDisplayName: Component, message: Component, viewer: Audience): Component {
        val playerHead = Noxesium.buildSkullComponent(source.uniqueId, false, 0, 0, 1.0f)
        val plainMessage = PlainTextComponentSerializer.plainText().serialize(message)
        return playerHead
            .append(allTags.deserialize("<tbdcolour>${source.name}<reset>: ")
                .append(if(source.hasPermission("tbd.group.admin")) allTags.deserialize(plainMessage) else restrictedTags.deserialize(plainMessage)))
    }
}