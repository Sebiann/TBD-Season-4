import event.PlayerFish
import event.ServerLinks
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class SeasonFourPlugin : JavaPlugin() {

    override fun onEnable() {
        println(this.name)
        this.logger.info("We are so back")
        setupEvents()
    }

    override fun onDisable() {
        this.logger.info("It is so over")
    }

    private fun setupEvents() {
        server.pluginManager.registerEvents(ServerLinks(), this)
        server.pluginManager.registerEvents(PlayerFish(), this)
    }

}

val plugin = Bukkit.getPluginManager().getPlugin("minecrafttwo")!!
val logger = plugin.logger
