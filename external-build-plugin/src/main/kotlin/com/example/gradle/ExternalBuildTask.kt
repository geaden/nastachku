package com.example.gradle

import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

@Slf4j
abstract class ExternalBuildTask : DefaultTask() {

    @get:Input
    abstract val target: Property<String>

    @get:Input
    abstract val dir: Property<File>

    @get:Input
    abstract val libName: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun execute() {
        val command = listOf<String>("bazel", "build", target.get())
        logger.info(command.joinToString(separator = " "))
        val p = ProcessBuilder(command)
            .directory(dir.get())
            .redirectErrorStream(true)
            .start()
        val stdout = buildString {
            p.inputReader().lines().forEach {
                logger.info(it)
                appendLine(it)
            }
        }
        if (p.waitFor() != 0) {
            throw GradleException("Failed to build target ${target.get()}\n${stdout}")
        }
    }
}
