package com.kenza.cloud

import com.kenza.cloud.CloudFactoryMod.Companion.MOD_ID
import com.kenza.cloud.block.Blocks.configCloudsBlocks
import com.kenza.cloud.block.Blocks.configsMachines
import com.kenza.cloud.block.CloudGeneratorBlockEntity
import com.kenza.cloud.block.CloudGeneratorHandler
import com.kenza.cloud.datagen.DataGeneratorManager
import com.kenza.cloud.mod.DiscsItems.onConfigDiscSongs
import com.kenza.cloud.item.ModItems.configCustomItems
import com.kenza.cloud.mod.Professions.configProfessions
import com.kenza.cloud.recipe.Recipes.configRecipes
import com.kenza.cloud.utils.openLastWorldOnInit
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager.getLogger

class CloudFactoryMod : ModInitializer {


    //data get entity @music_disc_cleopona.json SelectedItem
    //give @p iron_pickaxe{Damage:10000} 20
    //data get entity @s SelectedItem
    // You can also use F3+H in-game to show the ID's of items in-game.

    override fun onInitialize() {

        onConfig()
        openLastWorldOnInit()


        if (FabricLoader.getInstance().getLaunchArguments(true).contains("-dataGen")) {
            FabricDataGenHelper.run()
            ClientLifecycleEvents.CLIENT_STARTED.register(ClientLifecycleEvents.ClientStarted {
                DataGeneratorManager(MOD_ID).generate()
            })
        }

    }


    fun onConfig() {

        configCloudsBlocks()

        onConfigDiscSongs()
        configCustomItems()

        configRecipes()
        configsMachines()

        configProfessions()
//
//        configEffects()
//        configPotions()
    }


    companion object {


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
//    CloudFactoryMod.LOGGER.debug(msg)
    System.out.println(msg)
}