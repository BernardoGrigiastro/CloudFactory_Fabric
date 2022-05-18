package com.kenza.cloud.datagen.generators

import com.kenza.cloud.datagen.base.DataGenerator
import net.minecraft.util.Identifier
import java.io.File


class SlabBlockGenerator(
    val rootAssests: File,
    val rootData: File,
    namespace: String,
    fallback: (String) -> PattersFactory<String>
) : DataGenerator<String, String?>(File(rootAssests, "models/block"), namespace, fallback) {


    val blockstatesOutput = File(rootAssests, "blockstates")
    val loottablesOutput = File(rootData, "loot_tables/blocks")


    val tags = HashMap<String, String>()

    override fun generate(): Int {
        var count = 0
        generators.forEach { (key, _) ->
            val tag = tags[key]

            if (generate(Identifier(tag), key))
                count++

        }
        return count /4
    }

    fun register(material: String) {

        listOf(
            POSTFIX,
            TOP_POSTFIX,
        ).map { postfix ->
            val tag = material + "_" + postfix
            tags[tag] = tag
            register(tag, PatternsFactory.MODELS_BLOCKS_PART(material, namespace, postfix))
        }

        val tagSlab = material + "_" + POSTFIX
        val key = "blockstates_$tagSlab"
        tags[key] = tagSlab
        register(key, PatternsFactory.BLOCKSTATES_PART(material, namespace, POSTFIX, blockstatesOutput))


        val key1 = "dropself_$tagSlab"
        tags[key1] = tagSlab
        register(key1, PatternsFactory.LOOTTABLES_SELFDROP(tagSlab, namespace, loottablesOutput))
    }

    companion object {
        val POSTFIX = "slab"
        val TOP_POSTFIX = "slab_top"
    }
}

