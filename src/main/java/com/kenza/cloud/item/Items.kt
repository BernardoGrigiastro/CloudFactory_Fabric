package com.kenza.cloud.item

import com.kenza.cloud.CloudFactoryMod
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object Items {


    fun configCustomItems() {

        Registry.register(Registry.ITEM, Identifier(CloudFactoryMod.MOD_ID, "alumentum"), AlumentumItem(FabricItemSettings().group(CloudFactoryMod.MOD_GROUP)))
        registerItem("pegasus_amulet")
        registerItem("pegasus_belt")
        registerItem("pegasus_ring")
    }



    private fun registerItem(name: String){
        Registry.register(Registry.ITEM, Identifier(CloudFactoryMod.MOD_ID, name), Item(FabricItemSettings().group(CloudFactoryMod.MOD_GROUP)))
    }
}