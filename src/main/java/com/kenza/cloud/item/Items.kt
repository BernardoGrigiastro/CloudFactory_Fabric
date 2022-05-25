package com.kenza.cloud.item

import com.google.common.collect.Maps
import com.kenza.cloud.CloudFactoryMod
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.registry.FuelRegistry
import net.minecraft.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object Items {


    lateinit var ALUMENTUM_ITEM: AlumentumItem
    lateinit var PEGASUS_AMULET_ITEM: PegasusTrinketItem
    lateinit var PEGASUS_BELT_ITEM: PegasusTrinketItem
    lateinit var PEGASUS_RING_ITEM: PegasusTrinketItem

    fun configCustomItems() {

        ALUMENTUM_ITEM = Registry.register(Registry.ITEM, Identifier(CloudFactoryMod.MOD_ID, "alumentum"), AlumentumItem(FabricItemSettings().group(CloudFactoryMod.MOD_GROUP)))

        FuelRegistry.INSTANCE.add(ALUMENTUM_ITEM, 1600 * 8)


        PEGASUS_AMULET_ITEM = Registry.register(Registry.ITEM, Identifier(
            CloudFactoryMod.MOD_ID, "pegasus_amulet"
        ), PegasusTrinketItem(FabricItemSettings().group(CloudFactoryMod.MOD_GROUP).maxCount(1)))

        PEGASUS_BELT_ITEM = Registry.register(Registry.ITEM, Identifier(
            CloudFactoryMod.MOD_ID, "pegasus_belt"
        ), PegasusTrinketItem(FabricItemSettings().group(CloudFactoryMod.MOD_GROUP).maxCount(1)))

        PEGASUS_RING_ITEM = Registry.register(Registry.ITEM, Identifier(
            CloudFactoryMod.MOD_ID, "pegasus_ring"
        ), PegasusTrinketItem(FabricItemSettings().group(CloudFactoryMod.MOD_GROUP).maxCount(1)))


//        registerItem("pegasus_amulet")
//        registerItem("pegasus_belt")
//        registerItem("pegasus_ring")
    }



    private fun registerItem(name: String): Item {
        return Registry.register(Registry.ITEM, Identifier(CloudFactoryMod.MOD_ID, name), Item(FabricItemSettings().group(CloudFactoryMod.MOD_GROUP).maxCount(1)))
    }
}