package com.kenza.cloud.datagen.generators

import com.kenza.cloud.datagen.base.DataGenerator
import com.kenza.cloud.datagen.generators.PatternsFactory.BLOCKSTATES_PART
import com.kenza.cloud.datagen.generators.PatternsFactory.LOOTTABLES_SELFDROP
import com.kenza.cloud.datagen.generators.PatternsFactory.MODELS_BLOCKS_PART
import com.kenza.cloud.datagen.generators.PatternsFactory.RECIPES
import net.minecraft.util.Identifier
import java.io.File

class StairsBlockGenerator(
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
        return count / 6
    }

    fun register(material: String) {
        listOf(POSTFIX, INNER_POSTFIX, OUTHER_POSTFIX).map { postfix ->
            val tag = material + "_" + postfix
            tags[tag] = tag
            register(tag, MODELS_BLOCKS_PART(material, namespace, postfix))
        }

        val tagStairs = material + "_" + POSTFIX
        val key = "blockstates_$tagStairs"
        tags[key] = tagStairs
        register(key, BLOCKSTATES_PART(material, namespace, POSTFIX, blockstatesOutput))


        val key1 = "dropself_$tagStairs"
        tags[key1] = tagStairs
        register(key1, LOOTTABLES_SELFDROP(tagStairs,namespace, loottablesOutput))

        val key2 = "recipes_$tagStairs"
        tags[key2] = tagStairs
        val tagMaterial = "${material}_block"

        register(key2, RECIPES(tagStairs,namespace,  tagMaterial, POSTFIX, recipesOutput))

    }

    companion object {

        val POSTFIX = "stairs"
        val INNER_POSTFIX = "stairs_inner"
        val OUTHER_POSTFIX = "stairs_outer"


    }
}

