package com.kenza.cloud.datagen

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.kenza.cloud.CloudFactoryMod.Companion.LOGGER
import com.kenza.cloud.block.clouds.CloudBlock
import com.kenza.cloud.datagen.base.ImageFactory
import com.kenza.cloud.datagen.base.JsonFactory
import com.kenza.cloud.datagen.generators.BlockGenerator
import com.kenza.cloud.datagen.old.*
import com.kenza.cloud.datagen.generators.PattersFactory
import com.kenza.cloud.datagen.generators.SlabBlockGenerator
import com.kenza.cloud.datagen.generators.StairsBlockGenerator
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.io.File
import kotlin.system.exitProcess

class DataGeneratorManager(namespace: String) {

    val rootAssests = File("../src/main/generated/assets/cloud_factory")
    val rootData = File("../src/main/generated/data/cloud_factory")

    val lootTableGenerator = LootTableGenerator(rootData, namespace, LootTableGenerator.SELF_DROP)
    val itemModelGenerator = ItemModelGenerator(rootAssests, namespace, ItemModelGenerator.DEFAULT_ITEM)
    val blockModelGenerator = BlockModelGenerator(rootAssests, namespace) { JsonFactory.nullFactory() }
    val materialRecipeGenerator = MaterialRecipeGenerator(rootAssests, namespace) { JsonFactory.nullFactory() }
    val materialTagGenerator = MaterialTagGenerator(rootAssests, namespace) { JsonFactory.nullFactory() }
    val metalSpriteGenerator = MetalSpriteGenerator(rootAssests, namespace) { ImageFactory.nullFactory() }

    val stairsBlockGenerator = StairsBlockGenerator(rootAssests, rootData, namespace, PattersFactory.nullFactory())
    val slabBlockGenerator = SlabBlockGenerator(rootAssests, rootData, namespace, PattersFactory.nullFactory())
    val blockGenerator = BlockGenerator(rootAssests, rootData, namespace, PattersFactory.nullFactory())

    init {
        rootAssests.mkdir()
        rootData.mkdir()


        DyeColor.values().forEach {
            val color = it.getName()
            stairsBlockGenerator.register("cloud_$color")
            slabBlockGenerator.register("cloud_$color")
            blockGenerator.register("cloud_$color")
        }


//        arrayOf("tin", "lead", "tungsten", "silver").forEach { material ->
//            materialRecipeGenerator.register("raw_${material}", rawOreIntoBlock(material))
//            materialRecipeGenerator.register("raw_${material}_block", rawOreBlockIntoRawItem(material))
//        }
//
//        arrayOf("copper", "tin", "lead", "tungsten", "silver").forEach { material ->
//            materialRecipeGenerator.register(
//                "${material}_ore",
//                pulverizeOre("c:${material}_ores", "indrev:${material}_dust")
//            )
//            materialRecipeGenerator.register(
//                "${material}_ingot",
//                pulverizeIngot("c:${material}_ingots", "indrev:${material}_dust")
//            )
//            arrayOf("ore", "plate", "dust", "ingot").forEach { suffix ->
//                materialTagGenerator.register("${material}_$suffix", createTag("indrev:${material}_$suffix"))
//            }
//        }
//
//        arrayOf("iron", "gold").forEach { material ->
//            materialRecipeGenerator.register(
//                "${material}_ore",
//                pulverizeOre("c:${material}_ores", "indrev:${material}_dust")
//            )
//            materialRecipeGenerator.register(
//                "${material}_ingot",
//                pulverizeIngot("c:${material}_ingots", "indrev:${material}_dust")
//            )
//            materialTagGenerator.register("${material}_ore", createTag("minecraft:${material}_ore"))
//        }
//        arrayOf("diamond", "coal").forEach { material ->
//            materialRecipeGenerator.register(
//                "${material}_ore",
//                pulverizeOre("c:${material}_ores", "minecraft:${material}")
//            )
//            materialRecipeGenerator.register(
//                material,
//                pulverizeIngot("minecraft:$material", "indrev:${material}_dust", fileSuffix = "dust")
//            )
//            materialTagGenerator.register("${material}_dust", createTag("indrev:${material}_dust"))
//            materialTagGenerator.register("${material}_ore", createTag("minecraft:${material}_ore"))
//        }
//
//        DyeColor.values().forEach {
//            val name = it.getName()
//            materialRecipeGenerator.register("harden_${name}_concrete_powder", hardenConcretePowder(name))
//        }

//        MachineRegistry.MAP.values.distinct().forEach { registry ->
//            if (registry.upgradeable) {
//                arrayOf(Tier.MK1, Tier.MK2, Tier.MK3).forEach { tier ->
//                    materialRecipeGenerator.register("${Registry.ITEM.getId(registry.block(Tier.MK1).asItem()).toString().replace("_mk1", "")}_${tier.id}_to_${tier.next().id}", upgradeMachineRecipe(registry, tier, tier.next()))
//                }
//            }
//        }
//
//        itemModelGenerator.register(IRItemRegistry.GAMER_AXE_ITEM, JsonFactory.nullFactory())
//        itemModelGenerator.register(IRBlockRegistry.DRILL_BOTTOM.asItem(), JsonFactory.nullFactory())

//        Registry.BLOCK.forEach { block ->
//            val id = Registry.BLOCK.getId(block)
//            if (id.namespace == namespace && id.path.contains("cloud_"))
//                lootTableGenerator.register(block, LootTableGenerator.SELF_DROP(block))
//
//            if (block is CloudBlock) {
//                blockModelGenerator.register(block, BlockModelGenerator.CUBE_ALL(block))
//            }
//
//        }

//        MetalSpriteRegistry.MATERIAL_PROVIDERS.forEach { (id, model) ->
//            val itemId = Identifier(id.namespace, id.path)
//            val item = Registry.ITEM.get(itemId)
//            if (item != Items.AIR) {
//                if (item is BlockItem) {
//                    blockModelGenerator.register(item.block, BlockModelGenerator.CUBE_ALL(item.block))
//                }
//                if (id.toString().contains("sword"))
//                    metalSpriteGenerator.register(id, ImageFactory.simpleFactory0<Identifier>()(id))
//                else
//                    metalSpriteGenerator.register(id, ImageFactory.simpleFactory<Identifier>()(id))
//                val factory =
//                    if (model.type == MetalModel.TransformationType.HANDHELD) ItemModelGenerator.HANDHELD
//                    else ItemModelGenerator.DEFAULT_ITEM
//                itemModelGenerator.register(item, factory(item))
//            }
//        }
    }

    fun generate() {
        val lootTablesGenerated = lootTableGenerator.generate()
        LOGGER.info("Generated $lootTablesGenerated loot tables.")
        val itemModelsGenerated = itemModelGenerator.generate()
        LOGGER.info("Generated $itemModelsGenerated item models.")
        val blockModelsGenerated = blockModelGenerator.generate()
        LOGGER.info("Generated $blockModelsGenerated block models.")
        val recipesGenerated = materialRecipeGenerator.generate()
        LOGGER.info("Generated $recipesGenerated recipes.")
        val tagsGenerated = materialTagGenerator.generate()
        LOGGER.info("Generated $tagsGenerated tags.")
        val spritesGenerated = metalSpriteGenerator.generate()
        LOGGER.info("Generated $spritesGenerated sprites.")

        val stairsGenerated = stairsBlockGenerator.generate()
        val stairsGenerated2 = slabBlockGenerator.generate()
        val stairsGenerated3 = blockGenerator.generate()

        LOGGER.info("Generated $stairsGenerated  stairs.")
        LOGGER.info("Generated $stairsGenerated2 slabs.")
        LOGGER.info("Generated $stairsGenerated3 blocks.")

//        LOGGER.info("Generated ${lootTablesGenerated + itemModelsGenerated + blockModelsGenerated + recipesGenerated + tagsGenerated + spritesGenerated } files in total.")
        exitProcess(0)
    }

    operator fun Int.plus(boolean: Boolean): Int {
        return if (boolean) this + 1 else this
    }


}