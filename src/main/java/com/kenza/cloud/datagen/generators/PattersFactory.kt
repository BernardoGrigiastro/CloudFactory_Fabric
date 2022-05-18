package com.kenza.cloud.datagen.generators

import com.kenza.cloud.datagen.base.DataFactory
import com.kenza.cloud.datagen.base.ImageFactory
import java.awt.image.BufferedImage
import java.io.File

abstract class PattersFactory<T> : DataFactory<T, String?> {

//    val patternsDir = File("../src/main/patterns")

    override val extension: String get() = "json"

    override fun write(file: File, t: String?) {
//        file.writeText(GsonBuilder().setPrettyPrinting().create().toJson(t))
        file.writeText(t ?: "")
    }

    fun <T> nullFactory(): ImageFactory<T> = object : ImageFactory<T> {
        override fun generate(): BufferedImage? = null
    }

    companion object {
        fun nullFactory(): ((String) -> PattersFactory<String>) = { x ->
            object : PattersFactory<String>() {
                override fun generate(): String? = null
            }
        }



    }
}