package util

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.sound.Sound.sound

object Sounds {
    val EPIC_CATCH = sound(Key.key("entity.wither.spawn"), Sound.Source.VOICE, 0.5f, 1.25f)
    val LEGENDARY_CATCH = sound(Key.key("entity.ender_dragon.death"), Sound.Source.VOICE, 0.3f, 2f)
    val LEGENDARY_CATCH_EXPLODE = sound(Key.key("entity.generic.explode"), Sound.Source.VOICE, 1f, 1f)
    val MYTHIC_CATCH = sound(Key.key("block.portal.travel"), Sound.Source.VOICE, 0.5f, 2f)
    val UNREAL_CATCH = sound(Key.key("ambient.cave"), Sound.Source.VOICE, 10f, 2f)
    val UNREAL_CATCH_SPAWN = sound(Key.key("entity.warden.sonic_boom"), Sound.Source.VOICE, 2f, 2f)
    val UNREAL_CATCH_SPAWN_BATS = sound(Key.key("entity.warden.death"), Sound.Source.VOICE, 2f, 1f)
    val SHINY_CATCH = sound(Key.key("block.amethyst_cluster.step"), Sound.Source.VOICE, 2f, 2f)
    val CAMPFIRE_DISALLOW_FISH_COOK = sound(Key.key("block.fire.extinguish"), Sound.Source.BLOCK, 1f, 0f)
    val ENDER_EYE_PLACE_FAIL = sound(Key.key("block.end_portal_frame.fill"), Sound.Source.BLOCK, 2f, 0.5f)
    val ENDER_EYE_PLACE_FAIL_BACKGROUND = sound(Key.key("entity.lightning_bolt.thunder"), Sound.Source.BLOCK, 1f, 2f)
    val TRUE_EYE_SPAWN = sound(Key.key("block.end_portal.spawn"), Sound.Source.MASTER, 1f, 0.75f)
    val ADMIN_MESSAGE = sound(Key.key("ui.button.click"), Sound.Source.MASTER, 0.5f, 2f)
    val SERVER_ANNOUNCEMENT = sound(Key.key("block.note_block.pling"), Sound.Source.MASTER, 1f, 1f)
    val RENAME_ITEM = sound(Key.key("block.smithing_table.use"), Sound.Source.PLAYER, 1f, 1f)
    val FRAME_EYE_BREAK = sound(Key.key("entity.ender_eye.death"), Sound.Source.BLOCK, 1f, 0.75f)
}