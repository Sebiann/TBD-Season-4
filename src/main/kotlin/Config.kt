import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.io.File
import java.net.URI

@ConfigSerializable
data class Config(
    val links: List<Link>,
    val resourcePacks: List<ResourcePack>
)

@ConfigSerializable
data class Link(val component: String, val uri: URI, val order: Int)

@ConfigSerializable
data class ResourcePack(val uri: URI, val hash: String, val priority: Int)

object Memory {
    private const val MEMORY_PATH = "memories"
    private var loadedMemories = mutableListOf<ItemStack>()
    private val configFile = File(plugin.dataFolder, "config.yml")
    fun loadMemories() {
        loadedMemories.clear()
        plugin.config.load(configFile)
        val rawMemories = plugin.config.getList(MEMORY_PATH) ?: emptyList()
        loadedMemories = rawMemories.mapNotNull { it as? ItemStack }.toMutableList()
        logger.info("Memories loaded.")

        if(loadedMemories.contains(ItemStack(Material.AIR).asOne())) {
            logger.warning("Memories contain illegal entries, please action.")
        }
    }

    fun saveMemory(item: ItemStack) {
        if(loadedMemories.contains(item)) return
        if(item.type == Material.AIR) return
        val newMemoriesList = loadedMemories + item
        plugin.config.set(MEMORY_PATH, newMemoriesList)
        plugin.config.save(configFile)
        logger.info("A memory has been saved.")
        loadMemories()
    }

    fun getMemories(): List<ItemStack> {
        return loadedMemories
    }
}
