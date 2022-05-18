package com.kenza.cloud.block

import com.kenza.cloud.CloudFactoryMod
import com.kenza.cloud.CloudFactoryMod.Companion.CLOUD_GENERATOR_HANDLER
import com.kenza.cloud.block.clouds.*
import com.kenza.cloud.item.CloudBlockItem
import com.kenza.cloud.makeID
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricMaterialBuilder
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.MapColor
import net.minecraft.block.Material
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import registerScreenHandler

object Blocks {

    lateinit var CLOUD_GENERATOR_ITEM: BlockItem

    var CLOUD_BLOCKS = ArrayList<Block>()

    var CLOUD_STAIRS_BLOCKS = ArrayList<Block>()

    var CLOUD_SLAB_BLOCKS = ArrayList<Block>()

    var CLOUD_WALL_BLOCKS = ArrayList<Block>()

    var CLOUD_FENCE_BLOCKS = ArrayList<Block>()

    var CLOUD_GATE_BLOCKS = ArrayList<Block>()


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

    val CLOUD = FabricMaterialBuilder(MapColor.WHITE).notSolid().allowsMovement().lightPassesThrough().destroyedByPiston().build()

    val fabricSettings by lazy {
        FabricBlockSettings.of(CLOUD).strength(0.25f).hardness(0.2f).sounds(BlockSoundGroup.SNOW)
//        FabricBlockSettings.of(Material.LEAVES).strength(0.25f).hardness(0.2f).sounds(BlockSoundGroup.SNOW)
            .dynamicBounds()
            .nonOpaque()
    }


    fun configsMachines() {
        CLOUD_GENERATOR_HANDLER = CloudGeneratorBlock.CLOUD_GENERATOR_ID.registerScreenHandler(::CloudGeneratorHandler)

        val CLOUD_GENERATOR_BLOCK = CloudGeneratorBlock(
            FabricBlockSettings.of(Material.STONE)
                .requiresTool()
                .luminance { state ->
                    return@luminance if (state.get(CloudGeneratorBlock.ACTIVE)) 15 else 0
                }
//                .hardness(2f)
                .strength(2f),
            ::CloudGeneratorHandler
        )


        FabricBlockEntityTypeBuilder.create(
            { pos: BlockPos, state: BlockState ->
                CloudGeneratorBlockEntity(pos, state)
            }, CLOUD_GENERATOR_BLOCK
        ).build(null).apply {
            CloudFactoryMod.CLOUD_GENERATOR_TYPE = this
        }


        Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            CloudGeneratorBlock.CLOUD_GENERATOR_ID,
            CloudFactoryMod.CLOUD_GENERATOR_TYPE
        )

        Registry.register(Registry.BLOCK, CloudGeneratorBlock.CLOUD_GENERATOR_ID, CLOUD_GENERATOR_BLOCK)

        CLOUD_GENERATOR_ITEM = BlockItem(CLOUD_GENERATOR_BLOCK, FabricItemSettings().group(CloudFactoryMod.MOD_GROUP))
        Registry.register(Registry.ITEM, CloudGeneratorBlock.CLOUD_GENERATOR_ID, CLOUD_GENERATOR_ITEM)
    }


    fun configCloudsBlocks() {

        CloudFactoryMod.MOD_GROUP = FabricItemGroupBuilder.build(
            makeID("group_tab")
        ) {
            CloudFactoryMod.DEFAULT_TAB_GROUP_ITEM
        }


        MOD_COLORS.forEach { color ->

            val blockName = "cloud_${color}_block"
            val cloudBLock = registerCloudBlockItem(blockName) ?: return@forEach
            val item = registerBlockItem(blockName, cloudBLock)

            if (color == "light_blue") {
                CloudFactoryMod.DEFAULT_TAB_GROUP_ITEM = item?.defaultStack
            }

            CLOUD_BLOCKS.add(cloudBLock)
        }


        MOD_COLORS.forEach { color ->
            registerCloudStairsItem(color)
        }

        MOD_COLORS.forEach { color ->
            registerCloudSlabItem(color)
        }

        MOD_COLORS.forEach { color ->
            registerCloudWallItem(color)
        }

        MOD_COLORS.forEach { color ->
            registerCloudFenceItem(color)
        }

        MOD_COLORS.forEach { color ->
            registerCloudGateItem(color)
        }
    }


    private fun registerCloudGateItem(color: String): Block? {
        val blockName = "cloud_${color}_fence_gate"

        val cloudBLock = CloudGate(
            fabricSettings
        )

        return registerBlock(blockName, cloudBLock)?.apply {
            CLOUD_GATE_BLOCKS.add(this)
        }
    }


    private fun registerCloudFenceItem(color: String): Block? {
        val blockName = "cloud_${color}_fence"

        val cloudBLock = CloudFence(
            fabricSettings
        )

        return registerBlock(blockName, cloudBLock)?.apply {
            CLOUD_FENCE_BLOCKS.add(this)
        }
    }


    private fun registerCloudWallItem(color: String): Block? {
        val blockName = "cloud_${color}_wall"

        val cloudBLock = CloudWall(
            fabricSettings
        )

        return registerBlock(blockName, cloudBLock)?.apply {
            CLOUD_WALL_BLOCKS.add(this)
        }
    }


    private fun registerCloudSlabItem(color: String): Block? {
        val blockName = "cloud_${color}_slab"

        val cloudBLock = CloudSlab(
            fabricSettings
        )

        return registerBlock(blockName, cloudBLock)?.apply {
            CLOUD_SLAB_BLOCKS.add(this)
        }
    }

    private fun registerCloudStairsItem(color: String): Block? {

        val blockName = "cloud_${color}_stairs"

        val cloudBLock = CloudStairs(
            CLOUD_BLOCKS.first().defaultState, fabricSettings
        )

        return registerBlock(blockName, cloudBLock)?.apply {
            CLOUD_STAIRS_BLOCKS.add(this)
        }
    }


    private fun registerCloudBlockItem(blockName: String): Block? {


        val cloudBLock = CloudBlock(fabricSettings)

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