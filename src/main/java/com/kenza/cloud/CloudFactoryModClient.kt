package com.kenza.cloud

import com.kenza.cloud.CloudFactoryMod.Companion.CLOUD_BLOCKS
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.client.render.RenderLayer

class CloudFactoryModClient : ClientModInitializer {
    override fun onInitializeClient() {


        CLOUD_BLOCKS.map { block ->
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTranslucent())
        }


    }
}