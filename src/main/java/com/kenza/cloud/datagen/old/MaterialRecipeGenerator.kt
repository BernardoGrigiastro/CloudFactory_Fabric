package com.kenza.cloud.datagen.old

import com.google.gson.JsonObject
import com.kenza.cloud.datagen.base.DataGenerator
import com.kenza.cloud.datagen.base.JsonFactory
import net.minecraft.util.Identifier
import java.io.File

class MaterialRecipeGenerator(
    val root: File,
    namespace: String,
    fallback: (String) -> JsonFactory<String>
) : DataGenerator<String, JsonObject?>(File(root, "recipes"), namespace, fallback) {

    fun register(id: Identifier, factory: JsonFactory<String>) {
        generators[id.toString()] = factory
    }

    override fun generate(): Int {
        var count = 0
        generators.forEach { (tag, _) ->
            if (generate(Identifier(tag), tag))
                count++
        }
        return count
    }

    companion object
}