package command

import fishing.FishRarity
import fishing.Fishing

import io.papermc.paper.command.brigadier.CommandSourceStack

import net.kyori.adventure.text.Component

import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import plugin

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Debug {
    @Command("debug simulate_catch <rarity> <shiny>")
    @Permission("tbdseason4.command.debug")
    fun echo(css: CommandSourceStack, @Argument("rarity") rarity: FishRarity, @Argument("shiny") shiny: Boolean) {
        if(css.sender is Player) {
            css.sender.sendMessage(Component.text("Simulating catch of rarity $rarity"))
            val player = css.sender as Player
            val loc = player.location
            object : BukkitRunnable() {
                override fun run() {
                    val item = loc.world.spawn(loc, Item::class.java)
                    item.itemStack = ItemStack(Material.POTATO, 1)
                    Fishing.playerCaughtFish(player, item, item.location, rarity, shiny)
                }
            }.runTaskLater(plugin, 100L)
        }
    }
}