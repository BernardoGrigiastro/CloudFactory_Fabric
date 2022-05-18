package com.kenza.cloud.datagen.base

import net.minecraft.util.Identifier
import java.io.File

interface DataFactory<T, P> {

    val extension: String

    fun getFileName(t: T, id: Identifier): String = id.path

    fun generate(): P?

    fun write(file: File, t: P)

    fun getOutputDir(): File? = null

}