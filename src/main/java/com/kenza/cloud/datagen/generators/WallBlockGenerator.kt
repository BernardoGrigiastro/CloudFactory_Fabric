package com.kenza.cloud.datagen.generators

import com.kenza.cloud.datagen.base.DataGenerator
import net.minecraft.util.Identifier
import java.io.File


class WallBlockGenerator(
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
        listOf(POSTFIX1, POSTFIX2, POSTFIX3, POSTFIX4).map { postfix ->
            val tag = material + "_" + postfix
            tags[tag] = tag
            register(tag, PatternsFactory.MODELS_BLOCKS_PART(material, namespace, postfix))
        }

        val tagWall = material + "_" + POSTFIX0
        val tagWallInventory = material + "_" + POSTFIX4
        val key = "blockstates_$tagWall"
        tags[key] = tagWall
        register(key, PatternsFactory.BLOCKSTATES_PART(material, namespace, POSTFIX0, blockstatesOutput))





        val key1 = "dropself_$tagWall"
        tags[key1] = tagWall
        register(key1, PatternsFactory.LOOTTABLES_SELFDROP(tagWall, namespace, loottablesOutput))


        val key2 = "item_$tagWall"
        tags[key2] = tagWall
        register(key2, PatternsFactory.MODELS_ITEMS_PART(tagWallInventory, namespace, PatternsFactory.DEFAULT_POSTFIX))

//        val key2 = "recipes_$tagStairs"
//        tags[key2] = tagStairs
//        val tagMaterial = "${material}_block"
//
//        register(key2, PatternsFactory.RECIPES(tagStairs, namespace, tagMaterial, POSTFIX, recipesOutput))

    }

    companion object {

        val POSTFIX0 = "wall"

        val POSTFIX1 = "wall_post"
        val POSTFIX2 = "wall_side"
        val POSTFIX3 = "wall_side_tall"
        val POSTFIX4 = "wall_inventory"


    }
}