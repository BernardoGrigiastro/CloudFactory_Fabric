package com.kenza.cloud.mod

import com.kenza.cloud.CloudFactoryMod.Companion.MOD_GROUP
import com.kenza.cloud.CloudFactoryMod.Companion.MOD_ID
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.MusicDiscItem
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry

object DiscsItems {

    val SONGS_IDS = listOf(
        "spectacular",
        "adventurous_pony",
        "archaic_adventure",
        "beneath_clouds",
        "determined_to_fly",
        "electro_horse",
        "space_junk_galaxy",
        "night_out",
        "midnight"
    )

    val SONGS_ITEMS = ArrayList<MusicDiscItem>()


//
//    val BAROQUE_NIGHTMARE = registerSoundEvent("record.spectacular")
//
//    @kotlin.jvm.JvmField
//    val BAROQUE_NIGHTMARE_ITEM = registerDisc("music_disc_spectacular", BAROQUE_NIGHTMARE)

    fun onConfigDiscSongs() {

        SONGS_IDS.map { music ->
            val record = "record.$music"
            val sound = registerSoundEvent(record)

            val name = "music_disc_$music"
            registerDisc(name, sound).apply {
                SONGS_ITEMS.add(this)
            }
        }

    }

    fun registerSoundEvent(path: String?): SoundEvent {
        val id = Identifier(MOD_ID, path)
        return Registry.register(Registry.SOUND_EVENT, id, SoundEvent(id))
    }


    private fun <T : Item?> registerItem(name: String?, item: T): T {
        val id = Identifier(MOD_ID, name)
        if (item is BlockItem) {
            (item as BlockItem).appendBlocks(Item.BLOCK_ITEMS, item)
        }

        return Registry.register(Registry.ITEM, id, item)
    }

    private fun registerDisc(name: String?, sound: SoundEvent?): MusicDiscItem {
        return registerItem(name, object : MusicDiscItem(
            1, sound, Settings()
                .maxCount(1)
                .group(MOD_GROUP)
                .rarity(Rarity.RARE),1
        ) {})
    }

}