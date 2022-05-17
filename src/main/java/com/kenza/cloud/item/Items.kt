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


    fun configCustomItems() {

        val alumentum = Registry.register(Registry.ITEM, Identifier(CloudFactoryMod.MOD_ID, "alumentum"), AlumentumItem(FabricItemSettings().group(CloudFactoryMod.MOD_GROUP)))

        FuelRegistry.INSTANCE.add(alumentum, 1600 * 8)

        registerItem("pegasus_amulet")
        registerItem("pegasus_belt")
        registerItem("pegasus_ring")
    }



    private fun registerItem(name: String){
        Registry.register(Registry.ITEM, Identifier(CloudFactoryMod.MOD_ID, name), Item(FabricItemSettings().group(CloudFactoryMod.MOD_GROUP).maxCount(1)))
    }
}