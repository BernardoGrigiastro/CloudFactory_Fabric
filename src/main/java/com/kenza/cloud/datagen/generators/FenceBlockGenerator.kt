package com.kenza.cloud.datagen.generators

import com.kenza.cloud.datagen.base.DataGenerator
import net.minecraft.util.Identifier
import java.io.File

class FenceBlockGenerator(
    val rootAssests: File,
    val rootData: File,
    namespace: String,
    fallback: (String) -> PattersFactory<String>
) : DataGenerator<String, String?>(File(rootAssests, "models/block"), namespace, fallback) {


    val blockstatesOutput = File(rootAssests, "blockstates")
    val loottablesOutput = File(rootData, "loot_tables/blocks")
    val recipesOutput = File(rootData, "recipes")


    val tags = HashMap<String, String>()

    override fun generate(): Int {
        var count = 0
        generators.forEach { (key, _) ->
            val tag = tags[key]

            if (generate(Identifier(tag), key))
                count++

        }
        return count / tags.size
    }

    fun register(material: String) {
        listOf(POSTFIX1, POSTFIX2, POSTFIX4).map { postfix ->
            val tag = material + "_" + postfix
            tags[tag] = tag
            register(tag, PatternsFactory.MODELS_BLOCKS_PART(material, namespace, postfix))
        }

        val tagFence = material + "_" + POSTFIX0
        val tagInventory = material + "_" + POSTFIX4
        val key = "blockstates_$tagFence"
        tags[key] = tagFence
        register(key, PatternsFactory.BLOCKSTATES_PART(material, namespace, POSTFIX0, blockstatesOutput))





        val key1 = "dropself_$tagFence"
        tags[key1] = tagFence
        register(key1, PatternsFactory.LOOTTABLES_SELFDROP(tagFence, namespace, loottablesOutput))


        val key2 = "item_$tagFence"
        tags[key2] = tagFence
        register(key2, PatternsFactory.MODELS_ITEMS_PART(tagInventory, namespace, PatternsFactory.DEFAULT_POSTFIX))

        val key3 = "recipes_$tagFence"
        tags[key3] = tagFence
        val tagMaterial = "${material}_block"

        register(key3,
            PatternsFactory.RECIPES(tagFence, namespace, tagMaterial, POSTFIX_SNOW, recipesOutput)
        )
    }

    companion object {

        val POSTFIX_SNOW = "fence.snow"

        val POSTFIX0 = "fence"

        val POSTFIX1 = "fence_side"
        val POSTFIX2 = "fence_post"

        val POSTFIX4 = "fence_inventory"



    }
}