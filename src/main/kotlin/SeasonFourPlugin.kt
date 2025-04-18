import event.*
import event.player.PlayerFish
import event.player.PlayerInteract
import event.player.PlayerJoin
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.PaperCommandManager
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.io.File

@Suppress( "unstableApiUsage")
class SeasonFourPlugin : JavaPlugin() {
    private lateinit var commandManager: PaperCommandManager<CommandSourceStack>
    private lateinit var config: Config

    override fun onEnable() {
        this.logger.info("We are so back")
        readConfig()
        setupEvents()
        registerCommands()
    }

    override fun onDisable() {
        this.logger.info("It is so over")
    }

    private fun setupEvents() {
        server.pluginManager.registerEvents(ServerLinks(config), this)
        server.pluginManager.registerEvents(PlayerFish(), this)
        server.pluginManager.registerEvents(PlayerJoin(config), this)
        server.pluginManager.registerEvents(FurnaceSmelt(), this)
        server.pluginManager.registerEvents(PlayerInteract(), this)
    }

    private fun registerCommands() {
        commandManager = PaperCommandManager.builder()
            .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
            .buildOnEnable(this)

        val annotationParser = AnnotationParser(commandManager, CommandSourceStack::class.java)
        annotationParser.parseContainers()
    }

    private fun readConfig() {
        if (!dataFolder.exists()) dataFolder.mkdir()
        val configFile = File(dataFolder, "config.yml")

        if (!configFile.exists()) {
            getResource("config.yml").use { inputStream ->
                configFile.outputStream().use { outputStream ->
                    inputStream!!.copyTo(outputStream)
                }
            }
        }

        val loader = YamlConfigurationLoader.builder()
            .file(configFile)
            .defaultOptions { options ->
                options.serializers { builder ->
                    builder.registerAnnotatedObjects(objectMapperFactory())
                }
            }
            .build()

        val node = loader.load()
        config = node.get(Config::class)!!
        logger.info("Loaded config")
    }
}

val plugin = Bukkit.getPluginManager().getPlugin("tbdseason4")!!
val logger = plugin.logger
