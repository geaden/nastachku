package com.example.gradle

import org.gradle.api.provider.Property
import java.io.File

interface ExternalBuildExtension {
    val target: Property<String>
    val dir: Property<File>
    val libName: Property<String>
}
