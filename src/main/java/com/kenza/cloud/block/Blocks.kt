package com.kenza.cloud.block

import com.kenza.cloud.CloudFactoryMod
import com.kenza.cloud.block.clouds.CloudBlock
import com.kenza.cloud.item.CloudBlockItem
import com.kenza.cloud.makeID
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object Blocks {


    val MOD_COLORS = listOf<String>(
        "white",
        "orange",
        "magenta",
        "light_blue",
        "yellow",
        "lime",
        "pink",
        "gray",
        "light_gray",
        "cyan",
        "purple",
        "blue",
        "brown",
        "green",
        "red",
        "black"
    ).sortedBy {
        it
    }




    fun configCloudsBlocks(){

        CloudFactoryMod.MOD_GROUP = FabricItemGroupBuilder.build(
            makeID("group_tab")
        ) {
            CloudFactoryMod.DEFAULT_TAB_GROUP_ITEM
        }


        MOD_COLORS.forEach { color ->

            val blockName = "cloud_$color"
            val cloudBLock = registerCloudBlockItem(color) ?: return@forEach
            val item = registerBlockItem(blockName,cloudBLock )

            if(color == "light_blue" ) {
                CloudFactoryMod.DEFAULT_TAB_GROUP_ITEM = item?.defaultStack
            }

            CloudFactoryMod.CLOUD_BLOCKS.add(cloudBLock)
        }

    }



    private fun registerCloudBlockItem(color : String): Block? {

        val blockName = "cloud_$color"

        val cloudBLock = CloudBlock(
            FabricBlockSettings.of(Material.POWDER_SNOW).strength(0.25f).sounds(BlockSoundGroup.SNOW)
                .dynamicBounds()
                .nonOpaque()
        )

        return registerBlockWithoutBlockItem(blockName, cloudBLock)
    }


    private fun registerBlockWithoutBlockItem(name: String, block: Block): Block? {
        return Registry.register(Registry.BLOCK, Identifier(CloudFactoryMod.MOD_ID, name), block)
    }

    private fun registerBlock(name: String, block: Block): Block? {
        registerBlockItem(name, block)
        return Registry.register(Registry.BLOCK, Identifier(CloudFactoryMod.MOD_ID, name), block)
    }

    private fun registerBlockItem(name: String, block: Block): Item? {
        return Registry.register(
            Registry.ITEM, Identifier(CloudFactoryMod.MOD_ID, name),
            CloudBlockItem(block, FabricItemSettings().group(CloudFactoryMod.MOD_GROUP))
        )
    }
}