package com.kenza.cloud.datagen.base

import net.minecraft.util.Identifier
import java.io.File

abstract class DataGenerator<T, P>(val defaultDir: File, val namespace: String, val fallback: (T) -> DataFactory<T, P>) {


    protected val generators = HashMap<T, DataFactory<T, P>>()

    init {
        defaultDir.mkdirs()
    }

    operator fun get(obj: T): DataFactory<T, P> = generators.getOrDefault(obj, fallback(obj))


    fun register(obj: T, factory: DataFactory<T, P>) {
        generators[obj] = factory
    }

    fun generate(identifier: Identifier, obj: T): Boolean {
        val jsonFactory = this[obj]

        return try {
            generate(identifier, obj, jsonFactory)
        }catch (e: Throwable){
            e.printStackTrace()
            false
        }
    }

    fun generate(identifier: Identifier, obj: T, factory: DataFactory<T, P>): Boolean {
        val outputDir = factory.getOutputDir() ?: defaultDir
        val file = File(outputDir, "${factory.getFileName(obj, identifier)}.${factory.extension}")
        file.parentFile.mkdirs()
        val output = factory.generate()
        if (output != null) {
            file.createNewFile()
            file.absolutePath
            factory.write(file, output)
            return true
        }
        return false
    }



    abstract fun generate(): Int
}