package util.messenger

import logger

import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*

class BrandMessenger : PluginMessageListener {
    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        try {
            val brand = String(message, Charset.defaultCharset()).substring(1)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            logger.info("(BRAND) ${player.name} joined using $brand.")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }
}