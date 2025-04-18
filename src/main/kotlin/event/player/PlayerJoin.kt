package event.player

import Config
import ResourcePack
import logger
import net.kyori.adventure.resource.ResourcePackInfo
import net.kyori.adventure.resource.ResourcePackRequest
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.MessageDigest

class PlayerJoin : Listener {
    private val mm = MiniMessage.miniMessage()
    val resourcePacks = mutableListOf<ResourcePackInfo>()

    constructor(config: Config) {
        loadResourcePacks(config.resourcePacks)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val resourcePackRequest = ResourcePackRequest.resourcePackRequest()
            .packs(resourcePacks)
            .prompt(mm.deserialize("<gradient:#ff77a8:#ff510c>Please download the required resource packs for TBD</gradient>"))
            .build()

        event.player.sendResourcePacks(resourcePackRequest)
    }

    private fun loadResourcePacks(configPacks: List<ResourcePack>) {
        configPacks.sortedBy { it.priority }.forEach {
            if (it.hash.isEmpty()) {
                logger.info("Hash missing for ${it.uri}, downloading...")
                val hash = getHashForUri(it.uri)
                logger.info("Calculated hash $hash for pack ${it.uri}")
                resourcePacks.add(
                    ResourcePackInfo.resourcePackInfo()
                        .uri(it.uri)
                        .hash(hash)
                        .build()
                )
            } else {
                resourcePacks.add(
                    ResourcePackInfo.resourcePackInfo()
                        .uri(it.uri)
                        .hash(it.hash)
                        .build()
                )
            }
        }
    }

    private fun getHashForUri(packURI: URI): String {
        val client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build()
        val request = HttpRequest.newBuilder(packURI).build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofByteArray())

        if (response.statusCode() != 200) throw RuntimeException("Resourcepack download $packURI failed")

        return sha1(response.body())
    }

    private fun sha1(data: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-1")
        val hashedBytes = digest.digest(data)
        return hashedBytes.joinToString("") { "%02x".format(it) }
    }
}
