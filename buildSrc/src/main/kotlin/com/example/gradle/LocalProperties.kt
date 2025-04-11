package com.example.gradle

import org.gradle.internal.extensions.stdlib.capitalized
import java.io.File
import java.util.Properties

object LocalProperties {

    lateinit var rootDir: File

    private val properties: Properties
        get() = Properties().apply {
            load(File("$rootDir/local.properties").bufferedReader())
        }

    val jniBuild: JniBuild
        get() = JniBuild.from(properties)
}

enum class JniBuild {
    Cmake,
    NdkBuild,
    ExternalBuild,
    ;

    companion object {
        fun from(properties: Properties): JniBuild {
            return JniBuild.valueOf("${properties["jniBuild"] ?: "cmake"}".capitalized())
        }
    }
}
