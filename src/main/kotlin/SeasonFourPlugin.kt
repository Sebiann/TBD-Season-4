import event.PlayerFish
import event.ServerLinks
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.PaperCommandManager

@Suppress( "unstableApiUsage")
class SeasonFourPlugin : JavaPlugin() {
    private lateinit var commandManager: PaperCommandManager<CommandSourceStack>

    override fun onEnable() {
        this.logger.info("We are so back")
        setupEvents()
        registerCommands()
    }

    override fun onDisable() {
        this.logger.info("It is so over")
    }

    private fun setupEvents() {
        server.pluginManager.registerEvents(ServerLinks(), this)
        server.pluginManager.registerEvents(PlayerFish(), this)
    }

    private fun registerCommands() {
        commandManager = PaperCommandManager.builder()
            .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
            .buildOnEnable(this)

        val annotationParser = AnnotationParser(commandManager, CommandSourceStack::class.java)
        annotationParser.parseContainers()
    }

}

val plugin = Bukkit.getPluginManager().getPlugin("minecrafttwo")!!
val logger = plugin.logger
