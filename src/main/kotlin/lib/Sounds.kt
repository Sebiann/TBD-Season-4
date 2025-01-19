package lib

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound

object Sounds {
    val LEGENDARY_CATCH = Sound.sound(Key.key("entity.ender_dragon.death"), Sound.Source.VOICE, 0.5f, 2f)
    val MYTHIC_CATCH = Sound.sound(Key.key("block.portal.travel"), Sound.Source.VOICE, 0.75f, 2f)
    val UNREAL_CATCH = Sound.sound(Key.key("ambient.cave"), Sound.Source.VOICE, 3f, 2f)
    val UNREAL_CATCH_SPAWN = Sound.sound(Key.key("entity.warden.sonic_boom"), Sound.Source.VOICE, 1f, 2f)
    val UNREAL_CATCH_SPAWN_BATS = Sound.sound(Key.key("entity.warden.death"), Sound.Source.VOICE, 2f, 1f)
}