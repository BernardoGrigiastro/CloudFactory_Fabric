package com.kenza.cloud.datagen.generators

import com.kenza.cloud.datagen.base.DataGenerator
import net.minecraft.util.Identifier
import java.io.File


class GateBlockGenerator(
    val rootAssests: File,
    val rootData: File,
    namespace: String,
    fallback: (String) -> PattersFactory<String>
) : DataGenerator<String, String?>(File(rootAssests, "models/block"), namespace, fallback) {


    val blockstatesOutput = File(rootAssests, "blockstates")
    val loottablesOutput = File(rootData, "loot_tables/blocks")
    val recipesOutput = File(rootData, "recipes")
    val modelItemsOutput = File(rootAssests, "models/block")



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
        listOf(POSTFIX0, POSTFIX1, POSTFIX2, POSTFIX3).map { postfix ->
//        listOf(POSTFIX0).map { postfix ->
            val tag = material + "_" + postfix
            tags[tag] = tag
            register(tag, PatternsFactory.MODELS_BLOCKS_PART(material, namespace, postfix))
        }

        val tagGate = material + "_" + POSTFIX0
        val key = "blockstates_$tagGate"
        tags[key] = tagGate
        register(key, PatternsFactory.BLOCKSTATES_PART(material, namespace, POSTFIX0, blockstatesOutput))
//
//
//
        val key1 = "dropself_$tagGate"
        tags[key1] = tagGate
        register(key1, PatternsFactory.LOOTTABLES_SELFDROP(tagGate, namespace, loottablesOutput))
//
//
        val key2 = "item_$tagGate"
        tags[key2] = tagGate
        register(key2, PatternsFactory.MODELS_ITEMS_PART(tagGate, namespace, PatternsFactory.DEFAULT_POSTFIX))


        val key3 = "recipes_$tagGate"
        tags[key3] = tagGate
        val tagMaterial = "${material}_block"

        register(key3,
            PatternsFactory.RECIPES(tagGate, namespace, tagMaterial, POSTFIX_SNOW, recipesOutput)
        )

    }

    companion object {

        val POSTFIX_SNOW = "gate.snow"

        val POSTFIX0 = "fence_gate"
        val POSTFIX1 = "fence_gate_wall"
        val POSTFIX2 = "fence_gate_open"
        val POSTFIX3 = "fence_gate_wall_open"


    }
}