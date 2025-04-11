package com.example.gradle

import com.android.build.api.dsl.AndroidSourceSet
import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.core.InternalBaseVariant
import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.create
import java.io.File

class ExternalBuildPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create<ExternalBuildExtension>("externalBuild")
        target.androidBuildVariants.configureEach {
            val buildVariant = name
            val taskPrefix = buildVariant.capitalized()
            val buildDir = "${target.projectDir}/externalBuild"
            val outputDir = File("$buildDir/$buildVariant")
            val externalBuildTask = target.tasks.register(
                "external${taskPrefix}Build",
                ExternalBuildTask::class.java
            ) {
                group = "external"
                this.target.set(extension.target)
                this.dir.set(extension.dir)
                this.outputDir.set(outputDir)
                this.libName.set(extension.libName)

                outputs.upToDateWhen { true }

                doLast {
                    target.copy {
                        from(target.file("${extension.dir.get()}/bazel-bin/cpp")) {
                            include("*.so")
                        }
                        // Path is important
                        // TODO(developer): Handle other ABIs
                        into(File("$outputDir/jniLibs/lib/arm64-v8a"))
                        dirPermissions {
                            unix("777")
                        }
                        filePermissions {
                            unix("777")
                        }
                    }
                }
            }
            target.tasks.register(
                "clean${taskPrefix}ExternalBuild",
                Delete::class.java
            ) {
                delete("$buildDir/externalBuild")
            }
            sourceSets.find { it.name == name }?.apply {
                (this as AndroidSourceSet).jniLibs.srcDir(File(outputDir, "jniLibs/lib"))
            }
            target.tasks.named("merge${taskPrefix}JniLibFolders")
                .dependsOn(externalBuildTask)
        }
    }

    private val Project.androidBuildVariants: DomainObjectSet<out InternalBaseVariant>
        get() = when (val android = properties["android"]) {
            is LibraryExtension -> android.libraryVariants
            is AppExtension -> android.applicationVariants
            else -> error("not an android project")
        }
}
