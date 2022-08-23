package com.kenza.cloud.datagen

import com.kenza.cloud.CloudFactoryMod.Companion.MOD_ID
import com.kenza.cloud.block.clouds.*
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Material
import net.minecraft.item.MusicDiscItem
import net.minecraft.tag.BlockTags
import net.minecraft.tag.ItemTags
import net.minecraft.util.registry.Registry

class CloudFactoryDatagen : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(datagen: FabricDataGenerator) {

        datagen.output

        datagen.addProvider(CloudFactoryDatagen::ModBlockTagProvider)
        datagen.addProvider(CloudFactoryDatagen::ModItemTagProvider)

    }

    class ModItemTagProvider(datagen: FabricDataGenerator) : FabricTagProvider.ItemTagProvider(datagen) {

        override fun generateTags() {

            Registry.ITEM.ids.forEach { id ->
                if (id.namespace == MOD_ID) {
                    val item = Registry.ITEM.get(id)

                    if (item is MusicDiscItem) {
                        getOrCreateTagBuilder(ItemTags.MUSIC_DISCS).add(item)
                        getOrCreateTagBuilder(ItemTags.CREEPER_DROP_MUSIC_DISCS).add(item)
                    }
                }
            }
        }

    }

    class ModBlockTagProvider(datagen: FabricDataGenerator) : FabricTagProvider.BlockTagProvider(datagen) {
        override fun generateTags() {
            Registry.BLOCK.ids.forEach { id ->
                if (id.namespace == MOD_ID) {
                    val block = Registry.BLOCK.get(id)
                    if (block.defaultState.material == Material.METAL || block.defaultState.material == Material.STONE) {
                        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(block)
                    }
                    if (block.defaultState.material == Material.WOOD) {
                        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(block)
                    }

                    if (block is CloudAttribute) {
                        getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE).add(block)
                    }

                    if (block is CloudWall) {
                        getOrCreateTagBuilder(BlockTags.WALLS).add(block)
                    }

                    if (block is CloudFence) {
                        getOrCreateTagBuilder(BlockTags.FENCES).add(block)
                    }

                    if (block is CloudGate) {
                        getOrCreateTagBuilder(BlockTags.FENCE_GATES).add(block)
                    }
                }
            }

        }

    }
}