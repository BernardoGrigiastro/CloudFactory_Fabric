//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package com.kenza.cloud.mod

import com.google.common.collect.ImmutableMap
import com.kenza.cloud.block.Blocks
import com.kenza.cloud.mod.DiscsItems.registerSoundEvent
import com.kenza.cloud.profession.BuyMusicForEmeraldsFactory
import com.kenza.cloud.profession.SellMusicForEmeraldsFactory
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.minecraft.village.TradeOffers.*
import net.minecraft.village.VillagerProfession
import net.minecraft.world.poi.PointOfInterestType

object Professions {


    val ENTITY_VILLAGER_WORK_CLOUD_TINKER = registerSoundEvent("profession.cloud_tinker")

    val CLOUD_TINKER_POI = PointOfInterestType.register("cloud_tinker", PointOfInterestType.getAllStatesOf(Blocks.CLOUD_GENERATOR_BLOCK), 1, 1)
    val CLOUD_TINKER = VillagerProfession.register("cloud_tinker", CLOUD_TINKER_POI, ENTITY_VILLAGER_WORK_CLOUD_TINKER)


    fun configProfessions() {

        val djLevel1 = arrayOf(
            BuyMusicForEmeraldsFactory(20, 5),
            SellMusicForEmeraldsFactory( 10, 5)
        )
        val djLevel2 = arrayOf<Factory>(
            SellMusicForEmeraldsFactory( 10, 10),
            SellMusicForEmeraldsFactory( 10, 10),
        )
        val djLevel3 = arrayOf<Factory>(
            SellMusicForEmeraldsFactory( 10, 10),
            SellMusicForEmeraldsFactory( 10, 10),
        )
        val djLevel4 = arrayOf<Factory>(
            SellMusicForEmeraldsFactory( 10, 10),
            SellMusicForEmeraldsFactory( 10, 10),
        )

        val djLevel5 = arrayOf<Factory>(
            SellMusicForEmeraldsFactory( 10, 10),
            SellMusicForEmeraldsFactory( 10, 10),
        )

        val djLevel6 = arrayOf<Factory>(
            SellMusicForEmeraldsFactory( 10, 10),
            SellMusicForEmeraldsFactory( 10, 10),
        )

        PROFESSION_TO_LEVELED_TRADE[CLOUD_TINKER] =
            toIntMap(
                ImmutableMap.of(
                    1,
                    djLevel1,
                    2,
                    djLevel2,
                    3,
                    djLevel3,
                    4,
                    djLevel4,
                    5,
                    djLevel5,
                    6,
                    djLevel6
                )
            )
    }

    private fun toIntMap(trades: ImmutableMap<Int, Array<Factory>>): Int2ObjectMap<Array<Factory>> {
        return Int2ObjectOpenHashMap(trades)
    }






}