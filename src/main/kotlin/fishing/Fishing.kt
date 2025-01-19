package fishing

import util.Keys.FISH_RARITY
import plugin
import item.ItemRarity
import item.ItemType
import item.SubRarity
import lib.Sounds

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title

import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

import java.time.Duration
import kotlin.math.cos
import kotlin.math.sin

import kotlin.random.Random

object Fishing {
    fun playerCaughtFish(player: Player, item: Item, location: Location, forcedFishRarity: FishRarity?, forcedFishShiny: Boolean?) {
        val fishRarity = forcedFishRarity ?: FishRarity.getRandomRarity()
        val isShiny = forcedFishShiny ?: SubRarity.isShiny()

        val fishMeta = item.itemStack.itemMeta
        fishMeta.displayName(Component.text(item.name).color(TextColor.fromHexString(fishRarity.itemRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
        fishMeta.lore(listOf(
                Component.text("${fishRarity.itemRarity.rarityGlyph}${if(isShiny) "${SubRarity.SHINY.subRarityGlyph}${ItemType.FISH.typeGlyph}" else ItemType.FISH.typeGlyph}").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                Component.text("Caught by ", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false).append(Component.text(player.name, NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
            )
        )
        fishMeta.persistentDataContainer.set(FISH_RARITY, PersistentDataType.STRING, fishRarity.name)
        if(isShiny) fishMeta.setEnchantmentGlintOverride(true)
        item.itemStack.setItemMeta(fishMeta)
        player.sendActionBar(Component.text("Caught ").append(Component.text("${fishRarity.itemRarity.name.uppercase()} ", TextColor.fromHexString(fishRarity.itemRarity.rarityColour), TextDecoration.BOLD)).append(Component.text("${item.itemStack.type.name}!")).decoration(TextDecoration.BOLD, false))
        if(fishRarity.itemRarity == ItemRarity.RARE || fishRarity.itemRarity == ItemRarity.EPIC || fishRarity.itemRarity == ItemRarity.LEGENDARY || fishRarity.itemRarity == ItemRarity.MYTHIC || fishRarity.itemRarity == ItemRarity.UNREAL) rareFishAnimation(player, item, location.add(0.0, 1.5, 0.0), fishRarity)
    }

    private fun rareFishAnimation(catcher: Player, item: Item, location: Location, fishRarity: FishRarity) {
        if(fishRarity.itemRarity == ItemRarity.EPIC || fishRarity.itemRarity == ItemRarity.LEGENDARY || fishRarity.itemRarity == ItemRarity.MYTHIC || fishRarity.itemRarity == ItemRarity.UNREAL) {
            for(player in Bukkit.getOnlinePlayers()) {
                player.sendMessage(Component.text("${catcher.name} caught ").append(Component.text("${fishRarity.itemRarity.name.uppercase()} ", TextColor.fromHexString(fishRarity.itemRarity.rarityColour), TextDecoration.BOLD)).append(item.itemStack.effectiveName().hoverEvent(item.itemStack)).decoration(TextDecoration.BOLD, false).append(Component.text("!")).decoration(TextDecoration.BOLD, false))
            }
        }
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
                    player.showTitle(Title.title(Component.text(fishRarity.itemRarity.name.uppercase(), TextColor.fromHexString(fishRarity.itemRarity.rarityColour), TextDecoration.BOLD), Component.text("${catcher.name} caught a ${item.itemStack.type.name}!"), Title.Times.times(Duration.ofSeconds(1L), Duration.ofSeconds(1L), Duration.ofSeconds(1L))))
                }
                for(i in 0..8) {
                    object : BukkitRunnable() {
                        override fun run() {
                            firework(location, flicker=true, trail=false, fishRarity.itemRarity.rarityColourRGB, if(i <= 6) FireworkEffect.Type.BALL_LARGE else FireworkEffect.Type.BALL, false)
                        }
                    }.runTaskLater(plugin, i * 2L)
                }
                for(i in 0..40) {
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
                                startLoc.world.spawnParticle(Particle.SOUL_FIRE_FLAME, startLoc, 75, 0.0, 0.0, 0.0, 0.35)
                                startLoc.world.spawnParticle(Particle.SCULK_SOUL, startLoc, 100, 0.0, 0.0, 0.0, 0.40)
                                for(player in Bukkit.getOnlinePlayers()) player.playSound(Sounds.UNREAL_CATCH_SPAWN_BATS)
                            }
                        }
                    }.runTaskLater(plugin, i * 2L)
                }
                for(i in 0..9) {
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
                            if(i == 9) {
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

    private fun unrealEffect(location: Location) {
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
                if(timer >= 10 * 20) {
                    for(soul in souls) soul.remove()
                    souls.clear()
                    this.cancel()
                } else {
                    timer++
                }
            }
        }.runTaskTimer(plugin, 0L, 1L)

        object : BukkitRunnable() {
            var loc = location.clone()
            var radius = 0.0
            var y = 0.0
            override fun run() {
                val x = radius * cos(y)
                val z = radius * sin(y)
                firework(Location(location.world, loc.x + x, loc.y + y, loc.z + z), flicker=false, trail=false, ItemRarity.UNREAL.rarityColourRGB, FireworkEffect.Type.BALL, false)

                y += if(y >= 2.0) 0.1 else 0.05
                radius += if(radius >= 1.5) 0.08 else 0.15

                if(y >= 25) {
                    cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, 1L)
    }

    private fun getSoul(location: Location) : Bat {
        val bat = location.world.spawnEntity(location, EntityType.BAT) as Bat
        bat.isAwake = true
        bat.isSilent = true
        bat.isInvisible = true
        bat.isInvulnerable = true
        bat.addScoreboardTag("soul.bat.${bat.uniqueId}")
        return bat
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