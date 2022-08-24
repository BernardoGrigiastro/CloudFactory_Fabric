//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package com.kenza.cloud.mod

import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet
import com.kenza.cloud.CloudFactoryMod
import com.kenza.cloud.CloudFactoryMod.Companion.MOD_ID
import com.kenza.cloud.block.Blocks
import com.kenza.cloud.block.Blocks.CLOUD_WHITE_BLOCK
import com.kenza.cloud.item.ModItems
import com.kenza.cloud.item.ModItems.PEGASUS_AMULET_ITEM
import com.kenza.cloud.item.ModItems.PEGASUS_BELT_ITEM
import com.kenza.cloud.item.ModItems.PEGASUS_RING_ITEM
import com.kenza.cloud.mod.DiscsItems.registerSoundEvent
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.fabricmc.fabric.api.`object`.builder.v1.trade.TradeOfferHelper
import net.fabricmc.fabric.mixin.`object`.builder.PointOfInterestTypeAccessor
import net.fabricmc.fabric.mixin.`object`.builder.VillagerProfessionAccessor
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.item.DyeItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.sound.SoundEvents
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraft.village.TradeOffer
import net.minecraft.village.TradeOffers.*
import net.minecraft.village.VillagerProfession
import net.minecraft.world.poi.PointOfInterestType


object Professions {


    val ENTITY_VILLAGER_WORK_CLOUD_TINKER = registerSoundEvent("profession.cloud_tinker")

    val CLOUD_TINKER_POI = registerPOI("cloud_tinker_poi", Blocks.CLOUD_GENERATOR_BLOCK)
    val CLOUD_TINKER = registerProfession(
        "cloud_tinker",
        CLOUD_TINKER_POI)



    fun configProfessions() {
        PointOfInterestTypeAccessor.callSetup(CLOUD_TINKER_POI);

        // 1 em -> 10 white cloud

        // 1 em -> 4-8 alumen
        // 24-35 em -> бижутерия

        // 2-4 пор -> 1 em
        // 1 em ->  5-8 color

        // 1 em ->  5-8 color
        // 1 em ->  5-8 color

        // 24-48 al ->  elytra


        TradeOfferHelper.registerVillagerOffers(
            CLOUD_TINKER, 1
        ) { factories ->
            factories.add { entity, random ->
                TradeOffer(
                    ItemStack(Items.EMERALD, nextBetween(1, 1)),
                    ItemStack(CLOUD_WHITE_BLOCK, 10),
                    20, 2, 0.02f
                )
            }
        }


        val bijouteries = listOf(
            PEGASUS_AMULET_ITEM,
            PEGASUS_BELT_ITEM,
            PEGASUS_RING_ITEM,
        )

        val colorDyes = DyeColor.values()

        TradeOfferHelper.registerVillagerOffers(
            CLOUD_TINKER, 2
        ) { factories ->
            factories.add { entity, random ->
                TradeOffer(
                    ItemStack(Items.EMERALD, 1),
                    ItemStack(ModItems.ALUMENTUM_ITEM, random.nextBetween(4, 8)),
                    20, 5, 0.02f
                )
            }

            factories.add(Factory { entity, random ->
                TradeOffer(
                    ItemStack(Items.EMERALD, random.nextBetween(24, 36)),
                    ItemStack(bijouteries[random.nextBetween(0, bijouteries.size - 1)], 1),
                    5, 10, 0.02f
                )
            })
        }


        TradeOfferHelper.registerVillagerOffers(
            CLOUD_TINKER, 3
        ) { factories ->
            factories.add { entity, random ->
                TradeOffer(
                    ItemStack(Items.GUNPOWDER, random.nextBetween(3, 5)),
                    ItemStack(Items.EMERALD, 1),
                    20, 3, 0.02f
                )
            }

            factories.add { entity, random ->
                entity.generateDyeOffer()
            }
        }


        TradeOfferHelper.registerVillagerOffers(
            CLOUD_TINKER, 4
        ) { factories ->

            factories.add { entity, random ->
                entity.generateDyeOffer()
            }

            factories.add { entity, random ->
                entity.generateDyeOffer()
            }
        }

        TradeOfferHelper.registerVillagerOffers(
            CLOUD_TINKER, 5
        ) { factories ->
            factories.add { entity, random ->
                entity.generateDyeOffer()
            }

            factories.add { entity, random ->
                TradeOffer(
                    ItemStack(Items.DIAMOND, random.nextBetween(24, 48)),
                    ItemStack(
                        Items.ELYTRA, 1
                    ),
                    5, 20, 0.02f
                )
            }
        }





    }


//        val djLevel1 = arrayOf(
//            BuyMusicForEmeraldsFactory(20, 5),
//            SellMusicForEmeraldsFactory( 10, 5)
//        )
//        val djLevel2 = arrayOf<Factory>(
//            SellMusicForEmeraldsFactory( 10, 10),
//            SellMusicForEmeraldsFactory( 10, 10),
//        )
//        val djLevel3 = arrayOf<Factory>(
//            SellMusicForEmeraldsFactory( 10, 10),
//            SellMusicForEmeraldsFactory( 10, 10),
//        )
//        val djLevel4 = arrayOf<Factory>(
//            SellMusicForEmeraldsFactory( 10, 10),
//            SellMusicForEmeraldsFactory( 10, 10),
//        )
//
//        val djLevel5 = arrayOf<Factory>(
//            SellMusicForEmeraldsFactory( 10, 10),
//            SellMusicForEmeraldsFactory( 10, 10),
//        )
//
//        val djLevel6 = arrayOf<Factory>(
//            SellMusicForEmeraldsFactory( 10, 10),
//            SellMusicForEmeraldsFactory( 10, 10),
//        )

//        PROFESSION_TO_LEVELED_TRADE[CLOUD_TINKER] =
//            toIntMap(
//                ImmutableMap.of(
//                    1,
//                    djLevel1,
//                    2,
//                    djLevel2,
//                    3,
//                    djLevel3,
//                    4,
//                    djLevel4,
//                    5,
//                    djLevel5,
//                    6,
//                    djLevel6
//                )
//            )
//}

    private fun toIntMap(trades: ImmutableMap<Int, Array<Factory>>): Int2ObjectMap<Array<Factory>> {
        return Int2ObjectOpenHashMap(trades)
    }


    fun registerProfession(name: String?, type: PointOfInterestType?): VillagerProfession? {
        return Registry.register(
            Registry.VILLAGER_PROFESSION, Identifier(MOD_ID, name),
            VillagerProfessionAccessor.create(
                name, type, ImmutableSet.of(), ImmutableSet.of(),
                ENTITY_VILLAGER_WORK_CLOUD_TINKER
            )
        )
    }

    fun registerPOI(name: String?, block: Block): PointOfInterestType? {
        return Registry.register(
            Registry.POINT_OF_INTEREST_TYPE, Identifier(MOD_ID, name),
            PointOfInterestTypeAccessor.callCreate(
                name,
                ImmutableSet.copyOf(block.stateManager.states), 1, 1
            )
        )
    }


}

private fun Entity.generateDyeOffer(): TradeOffer {

    val offeredItems = (this as VillagerEntity).offers.filter { it.sellItem.item is DyeItem }.map { it.sellItem.item as DyeItem }
    val dyes =   DyeColor.values().subtract(
        offeredItems.map {  it.color }
    )

    return TradeOffer(
        ItemStack(Items.EMERALD, 1),
        ItemStack(
            dyes.random().run {
                DyeItem.byColor(this)
            }, random.nextBetween(5, 8)
        ),
        20, 3, 0.02f
    )
}


fun Any.nextBetween(from: Int, to: Int) = (Math.random() * (to - from) + from).toInt()
