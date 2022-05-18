package com.kenza.cloud.datagen.generators

import java.io.File

object PatternsFactory {


    val patternsDir = File("../src/main/patterns")


    val MODELS_BLOCKS_PART: (String, String, String) -> PattersFactory<String> = { material, namespace, postfix ->

        object : PattersFactory<String>() {
            override fun generate(): String {
                val scheme = File(patternsDir, "models.block.${postfix}.txt").readText()
                val result = scheme
                    .replace("%1", material)
                    .replace("%2", namespace)

                return result
            }
        }
    }

    val BLOCKSTATES_PART: (String, String, String, File) -> PattersFactory<String> =
        { material, namespace, postfix, outputDir ->

            object : PattersFactory<String>() {
                override fun generate(): String {
                    val scheme = File(patternsDir, "blockstates.${postfix}.txt").readText()
                    val result = scheme
                        .replace("%1", material)
                        .replace("%2", namespace)

                    return result
                }

                override fun getOutputDir(): File = outputDir
            }
        }

    val LOOTTABLES_SELFDROP: (String, String, File) -> PattersFactory<String> =
        { item, namespace, outputDir ->

            object : PattersFactory<String>() {
                override fun generate(): String {
                    val scheme = File(patternsDir, "loot_tables.blocks.txt").readText()
                    val result = scheme
                        .replace("%1", item)
                        .replace("%2", namespace)

                    return result
                }

                override fun getOutputDir(): File = outputDir

            }
        }

    val RECIPES: (String, String,  String, String,  File) -> PattersFactory<String> =
        { item, namespace, material, postfix, outputDir ->

            object : PattersFactory<String>() {
                override fun generate(): String {
                    val scheme = File(patternsDir, "recipes.${postfix}.txt").readText()
                    val result = scheme
                        .replace("%1", item)
                        .replace("%2", namespace)
                        .replace("%3", material)
                        .replace("%4", namespace)

                    return result
                }

                override fun getOutputDir(): File = outputDir

            }
        }
}