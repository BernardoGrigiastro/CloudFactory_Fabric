package com.kenza.cloud

import com.kenza.cloud.CloudFactoryMod.Companion.MOD_ID
import com.kenza.cloud.block.Blocks.configCloudsBlocks
import com.kenza.cloud.block.CloudGeneratorBlock
import com.kenza.cloud.block.CloudGeneratorBlock.Companion.ACTIVE
import com.kenza.cloud.block.CloudGeneratorBlock.Companion.CLOUD_GENERATOR_ID
import com.kenza.cloud.block.CloudGeneratorBlockEntity
import com.kenza.cloud.block.CloudGeneratorHandler
import com.kenza.cloud.item.Items.configCustomItems
import com.kenza.cloud.recipe.Recipes.configRecipes
import com.kenza.cloud.utils.openLastWorldOnInit
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager.getLogger
import registerScreenHandler

class CloudFactoryMod : ModInitializer {


    //data get entity @music_disc_cleopona.json SelectedItem
    //give @p iron_pickaxe{Damage:10000} 20


    override fun onInitialize() {

        onConfig()
        openLastWorldOnInit()
    }


    fun onConfig() {

        configCloudsBlocks()
        configCustomItems()
        configRecipes()

        CLOUD_GENERATOR_HANDLER = CLOUD_GENERATOR_ID.registerScreenHandler(::CloudGeneratorHandler)

        val CLOUD_GENERATOR_BLOCK = CloudGeneratorBlock(
            FabricBlockSettings.of(Material.METAL)
                .requiresTool()
                .luminance { state ->
                    return@luminance if (state.get(ACTIVE)) 15 else 0
                }
                .strength(6f),
            ::CloudGeneratorHandler
        )


        FabricBlockEntityTypeBuilder.create(
            { pos: BlockPos, state: BlockState ->
                CloudGeneratorBlockEntity(pos, state)
            }, CLOUD_GENERATOR_BLOCK
        ).build(null).apply {
            CLOUD_GENERATOR_TYPE = this
        }


        Registry.register(Registry.BLOCK_ENTITY_TYPE, CLOUD_GENERATOR_ID, CLOUD_GENERATOR_TYPE)

        Registry.register(Registry.BLOCK, CLOUD_GENERATOR_ID, CLOUD_GENERATOR_BLOCK)

        val CLOUD_GENERATOR_ITEM =  BlockItem(CLOUD_GENERATOR_BLOCK, FabricItemSettings().group(MOD_GROUP))
        Registry.register(Registry.ITEM, CLOUD_GENERATOR_ID, CLOUD_GENERATOR_ITEM)

    }


    companion object {

        var CLOUD_BLOCKS = ArrayList<Block>()

        @JvmField
        var CLOUD_GENERATOR_HANDLER: ScreenHandlerType<CloudGeneratorHandler>? = null

        @JvmField
        var CLOUD_GENERATOR_TYPE: BlockEntityType<CloudGeneratorBlockEntity>? = null


        @JvmField
        var MOD_GROUP: ItemGroup? = null

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

fun makeID(path: String?): Identifier {
    return Identifier(MOD_ID, path)
}

fun Any.debug(msg: String) {
    CloudFactoryMod.LOGGER.debug(msg)
}