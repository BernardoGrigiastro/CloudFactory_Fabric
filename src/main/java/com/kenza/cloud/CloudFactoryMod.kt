package com.kenza.cloud

import com.kenza.cloud.CloudFactoryMod.Companion.MOD_ID
import com.kenza.cloud.utils.openLastWorldOnInit
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager.getLogger

class CloudFactoryMod : ModInitializer {


    //data get entity @music_disc_cleopona.json SelectedItem
    //give @p iron_pickaxe{Damage:10000} 20



    override fun onInitialize() {

        onConfig()
        openLastWorldOnInit()
    }


    fun onConfig() {


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



        GROUP_TAB = FabricItemGroupBuilder.build(
            makeID("group_tab")
        ) {
            DEFAULT_TAB_GROUP_ITEM
        }


        MOD_COLORS.forEach { color ->

            val blockName = "cloud_$color"
            val cloudBLock = registerCloudBlockItem(color) ?: return@forEach
            val item = registerBlockItem(blockName,cloudBLock )

            if(color == "light_blue" ) {
                DEFAULT_TAB_GROUP_ITEM = item?.defaultStack
            }

            CLOUD_BLOCKS.add(cloudBLock)
        }
    }

    fun registerCloudBlockItem(color : String): Block? {

        val blockName = "cloud_$color"

        val cloudBLock = CloudBlock(
            FabricBlockSettings.of(Material.POWDER_SNOW).strength(0.25f).sounds(BlockSoundGroup.SNOW)
                .dynamicBounds()
                .nonOpaque()
        )

        return registerBlockWithoutBlockItem(blockName, cloudBLock)
    }


    private fun registerBlockWithoutBlockItem(name: String, block: Block): Block? {
        return Registry.register(Registry.BLOCK, Identifier(MOD_ID, name), block)
    }

    private fun registerBlock(name: String, block: Block): Block? {
        registerBlockItem(name, block)
        return Registry.register(Registry.BLOCK, Identifier(MOD_ID, name), block)
    }

    private fun registerBlockItem(name: String, block: Block): Item? {
        return Registry.register(
            Registry.ITEM, Identifier(MOD_ID, name),
            BlockItem(block, FabricItemSettings().group(GROUP_TAB))
        )
    }


    companion object {

        var CLOUD_BLOCKS = ArrayList<Block>()

        @JvmField
        var GROUP_TAB: ItemGroup? = null

        @JvmField
        var DEFAULT_TAB_GROUP_ITEM: ItemStack? = null




        @JvmField
        val MOD_ID = "cloud_factory"


        @JvmField
        val LOGGER = getLogger("cloud_factory")


        @JvmStatic
        fun debug(msg: String) {
            System.out.println("cloud_factory $msg")
//            CloudFactoryMod.LOGGER.log( org.apache.logging.log4j.Level.ALL, msg)
        }


    }
}

fun makeID(path: String?): Identifier? {
    return Identifier(MOD_ID, path)
}

fun Any.debug(msg: String) {
    CloudFactoryMod.LOGGER.debug(msg)
}