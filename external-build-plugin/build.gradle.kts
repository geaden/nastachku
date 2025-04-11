plugins {
    alias(libs.plugins.kotlin.dsl)
}

gradlePlugin {
    plugins {
        create("external-build-plugin") {
            id = "com.example.external-build-plugin"
            implementationClass = "com.example.gradle.ExternalBuildPlugin"
        }
    }
}

dependencies {
    implementation(libs.android.library.plugin)
    implementation(libs.android.application.plugin)
}
