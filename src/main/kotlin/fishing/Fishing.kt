package fishing

import util.Keys.FISH_RARITY
import plugin
import item.ItemRarity
import item.ItemType
import item.SubRarity
import lib.Sounds

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.title.Title

import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

import java.time.Duration
import java.util.*

import kotlin.collections.ArrayList
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

object Fishing {
    private val mm = MiniMessage.miniMessage()
    private val applyCaughtLore = listOf(FishRarity.LEGENDARY, FishRarity.MYTHIC, FishRarity.UNREAL)
    private val runCatchAnimation = listOf(FishRarity.RARE, FishRarity.EPIC, FishRarity.LEGENDARY, FishRarity.MYTHIC, FishRarity.UNREAL)
    private val runCatchGlobalMessage = listOf(FishRarity.EPIC, FishRarity.LEGENDARY, FishRarity.MYTHIC, FishRarity.UNREAL)
    private val runCatchGlobalTitle = listOf(FishRarity.MYTHIC, FishRarity.UNREAL)

    fun catchFish(player: Player, item: Item, location: Location, forcedFishRarity: FishRarity?, forcedFishShiny: Boolean?) {
        val fishRarity = forcedFishRarity ?: FishRarity.getRandomRarity()
        val isShiny = forcedFishShiny ?: SubRarity.isShiny()

        val caughtByLore = if(applyCaughtLore.contains(fishRarity) || isShiny) mm.deserialize("<reset><white>Caught by <yellow>${player.name}<white>.").decoration(TextDecoration.ITALIC, false) else null
        val fishMeta = item.itemStack.itemMeta
        fishMeta.displayName(mm.deserialize("<${fishRarity.itemRarity.rarityColour}>${item.name}").decoration(TextDecoration.ITALIC, false))
        fishMeta.lore(
            if(caughtByLore == null) {
                listOf(mm.deserialize("<reset><white>${fishRarity.itemRarity.rarityGlyph}${ItemType.FISH.typeGlyph}").decoration(TextDecoration.ITALIC, false))
            } else {
                listOf(mm.deserialize("<reset><white>${fishRarity.itemRarity.rarityGlyph}${if (isShiny) "<reset><white>${SubRarity.SHINY.subRarityGlyph}${ItemType.FISH.typeGlyph}" else "<reset><white>${ItemType.FISH.typeGlyph}"}").decoration(TextDecoration.ITALIC, false), caughtByLore)
            }
        )
        if(isShiny) fishMeta.setEnchantmentGlintOverride(true)
        fishMeta.persistentDataContainer.set(FISH_RARITY, PersistentDataType.STRING, fishRarity.name)
        item.itemStack.setItemMeta(fishMeta)

        player.sendActionBar(mm.deserialize("Caught <${fishRarity.itemRarity.rarityColour}><b>${fishRarity.itemRarity.name.uppercase()}</b> ").append(item.itemStack.effectiveName()).append(mm.deserialize("<reset>.")))

        if(runCatchGlobalMessage.contains(fishRarity)) catchText(player, item, fishRarity)
        if(runCatchGlobalTitle.contains(fishRarity)) catchTitle(player, item, fishRarity)
        if(runCatchAnimation.contains(fishRarity)) catchAnimation(player, item, location.add(0.0, 1.75, 0.0), fishRarity)
    }

    private fun catchText(catcher: Player, item: Item, fishRarity: FishRarity) {
        if(runCatchGlobalMessage.contains(fishRarity)) {
            for(player in Bukkit.getOnlinePlayers()) {
                player.sendMessage(mm.deserialize("<${fishRarity.itemRarity.rarityColour}>${catcher.name}<reset> caught a${if(ItemRarity.startsWithVowel(fishRarity.itemRarity)) "n " else " "}<${fishRarity.itemRarity.rarityColour}><b>${fishRarity.name}</b> ${item.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}<reset>!"))
            }
        }
    }

    private fun catchTitle(catcher: Player, item: Item, fishRarity: FishRarity) {
        if(runCatchGlobalTitle.contains(fishRarity)) {
            for(player in Bukkit.getOnlinePlayers()) {
                player.showTitle(Title.title(mm.deserialize("<${fishRarity.itemRarity.rarityColour}><b>${fishRarity.itemRarity.rarityName.uppercase()}<reset>"), mm.deserialize("<${fishRarity.itemRarity.rarityColour}>${catcher.name}<reset> caught a${if(ItemRarity.startsWithVowel(fishRarity.itemRarity)) "n " else " "}<${fishRarity.itemRarity.rarityColour}><b>${fishRarity.name}</b> ${item.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}<reset>!"), Title.Times.times(Duration.ofSeconds(1L), Duration.ofSeconds(1L), Duration.ofSeconds(1L))))
            }
        }
    }

    private fun catchAnimation(catcher: Player, item: Item, location: Location, fishRarity: FishRarity) {
        when(fishRarity) {
            FishRarity.RARE -> {
                firework(location, flicker=false, trail=false, fishRarity.itemRarity.rarityColourRGB, FireworkEffect.Type.BURST, false)
            }
            FishRarity.EPIC -> {
                for(player in Bukkit.getOnlinePlayers()) player.playSound(Sounds.EPIC_CATCH)
                firework(location, flicker=false, trail=false, fishRarity.itemRarity.rarityColourRGB, FireworkEffect.Type.BALL, false)
                epicEffect(location)
            }
            FishRarity.LEGENDARY -> {
                for(player in Bukkit.getOnlinePlayers()) player.playSound(Sounds.LEGENDARY_CATCH)
                firework(location, flicker=true, trail=true, fishRarity.itemRarity.rarityColourRGB, FireworkEffect.Type.BALL_LARGE, false)
                legendaryEffect(location)
            }
            FishRarity.MYTHIC -> {
                for(player in Bukkit.getOnlinePlayers()) {
                    player.playSound(Sounds.MYTHIC_CATCH)
                }
                for(i in 0..25) {
                    object : BukkitRunnable() {
                        override fun run() {
                            firework(location, flicker=true, trail=false, fishRarity.itemRarity.rarityColourRGB, if(i <= 19) FireworkEffect.Type.BALL_LARGE else FireworkEffect.Type.BALL, false)
                        }
                    }.runTaskLater(plugin, i * 2L)
                }
                for(i in 0..60) {
                    object : BukkitRunnable() {
                        override fun run() {
                            firework(location, i % 2 == 0, i % 3 == 0, fishRarity.itemRarity.rarityColourRGB, if(i % 2 == 0) FireworkEffect.Type.BALL_LARGE else FireworkEffect.Type.BALL, true)
                        }
                    }.runTaskLater(plugin, (i * 3L) + 30L)
                }
            }
            FishRarity.UNREAL -> {
                for(player in Bukkit.getOnlinePlayers()) {
                    player.playSound(Sounds.UNREAL_CATCH)
                    player.playSound(Sounds.UNREAL_CATCH_SPAWN)
                    player.showTitle(Title.title(Component.text(fishRarity.itemRarity.name.uppercase(), TextColor.fromHexString(fishRarity.itemRarity.rarityColour), TextDecoration.BOLD), Component.text("${catcher.name} caught a ${item.itemStack.type.name}!"), Title.Times.times(Duration.ofSeconds(1L), Duration.ofSeconds(1L), Duration.ofSeconds(1L))))
                }
                val previousDayTime = catcher.world.time
                val previousFullTime = catcher.world.fullTime
                if(catcher.world.time < 6000) catcher.world.time += 6000 - catcher.world.time
                if(catcher.world.time > 6000) catcher.world.time -= 6000 + catcher.world.time
                for(i in 0..15) {
                    object : BukkitRunnable() {
                        val startLoc = item.location.clone()
                        override fun run() {
                            startLoc.world.spawnParticle(Particle.SONIC_BOOM, startLoc.add(0.0, i.toDouble(), 0.0), 1, 0.0, 0.0, 0.0, 0.0)
                            if(i == 15) {
                                unrealEffect(startLoc)
                                startLoc.world.spawnParticle(Particle.SOUL_FIRE_FLAME, startLoc, 100, 0.0, 0.0, 0.0, 0.35)
                                startLoc.world.spawnParticle(Particle.SCULK_SOUL, startLoc, 100, 0.0, 0.0, 0.0, 0.40)
                                for(player in Bukkit.getOnlinePlayers()) player.playSound(Sounds.UNREAL_CATCH_SPAWN_BATS)
                            }
                        }
                    }.runTaskLater(plugin, i * 2L)
                }
                for(i in 0..19) {
                    object : BukkitRunnable() {
                        override fun run() {
                            catcher.world.strikeLightningEffect(item.location.set(item.location.x, -64.0, item.location.z))
                            if(i % 2 == 0) {
                                catcher.world.time += 12000
                                item.isGlowing = true
                            } else {
                                catcher.world.time -= 12000
                                item.isGlowing = false
                            }
                            if(i == 19) {
                                catcher.world.fullTime = previousFullTime
                                catcher.world.time = previousDayTime
                            }
                        }
                    }.runTaskLater(plugin, i * 15L)
                }
            }
            else -> { /* do nothing */ }
        }
    }

    private fun epicEffect(location: Location) {
        object : BukkitRunnable() {
            var radius = 0.0
            override fun run() {
                if (radius > 4.0) {
                    cancel()
                    return
                }
                val step = Math.PI / 16
                for (angle in 0 until 32) {
                    val x = radius * cos(angle * step)
                    val z = radius * sin(angle * step)
                    val particleLocation = location.clone().add(x, -0.75, z)
                    location.world.spawnParticle(Particle.WITCH, particleLocation, 1, 0.0, 0.0, 0.0, 0.0)
                }
                radius += 0.2
            }
        }.runTaskTimer(plugin, 0L, 2L)
    }

    private fun legendaryEffect(location: Location) {
        val effectLoc = location.clone()
        for(i in 0..3) {
            object : BukkitRunnable() {
                override fun run() {
                    effectLoc.world.playSound(Sounds.LEGENDARY_CATCH_EXPLODE)
                    effectLoc.world.spawnParticle(Particle.EXPLOSION, effectLoc.add(Random.nextDouble(0.25, 0.5), Random.nextDouble(0.25, 0.5), Random.nextDouble(0.25, 0.5)), 1, 0.0, 0.0, 0.0, 0.0)
                }
            }.runTaskLater(plugin, (i * 4L) + 35L)
        }
    }

    private fun unrealEffect(location: Location) {
        fun getSoul(location: Location) : Bat {
            val bat = location.world.spawnEntity(location, EntityType.BAT) as Bat
            bat.isAwake = true
            bat.isSilent = true
            bat.isInvisible = true
            bat.isInvulnerable = true
            bat.addScoreboardTag("soul.bat.${bat.uniqueId}")
            return bat
        }
        object : BukkitRunnable() {
            val soulAmount = 20
            var timer = 0
            val souls = ArrayList<Bat>()
            override fun run() {
                if(timer <= soulAmount) {
                    val soul = getSoul(location)
                    souls.add(soul)
                    soul.velocity = Vector(0.0, 0.15, 0.0)
                }
                for(soul in souls) soul.world.spawnParticle(Particle.SCULK_SOUL, soul.location, 2, 0.0, 0.0, 0.0, 0.0)
                if(timer >= 14 * 20) {
                    for(soul in souls) soul.remove()
                    souls.clear()
                    this.cancel()
                } else {
                    timer++
                }
            }
        }.runTaskTimer(plugin, 0L, 1L)

        object : BukkitRunnable() {
            var i = 0
            var loc = location.clone()
            var radius = 0.0
            var y = 0.0
            override fun run() {
                val x = radius * cos(y)
                val z = radius * sin(y)
                if(i % 2 == 0) firework(Location(location.world, loc.x + x, loc.y + y, loc.z + z), flicker=false, trail=false, ItemRarity.UNREAL.rarityColourRGB, FireworkEffect.Type.BALL, false)

                y += if(y >= 2.0) 0.1 else 0.05
                radius += if(radius >= 1.5) 0.08 else 0.15

                if(y >= 25) cancel()
                i++
            }
        }.runTaskTimer(plugin, 0L, 1L)
    }

    private fun firework(location: Location, flicker: Boolean, trail: Boolean, rgb: Triple<Int, Int, Int>, fireworkType: FireworkEffect.Type, variedVelocity: Boolean) {
        val f: Firework = location.world.spawn(Location(location.world, location.x, location.y + 1.0, location.z), Firework::class.java)
        val fm = f.fireworkMeta
        fm.addEffect(
            FireworkEffect.builder()
                .flicker(flicker)
                .trail(trail)
                .with(fireworkType)
                .withColor(Color.fromRGB(rgb.first, rgb.second, rgb.third))
                .build()
        )
        if(variedVelocity) {
            fm.power = 1
            f.fireworkMeta = fm
            val direction = Vector(
                Random.nextDouble(-0.005, 0.005),
                Random.nextDouble(0.25, 0.35),
                Random.nextDouble(-0.005, 0.005)
            ).normalize()
            f.velocity = direction
        } else {
            fm.power = 0
            f.fireworkMeta = fm
            f.ticksToDetonate = 1
        }
    }
}